package com.drumonii.loltrollbuild.rest.specification;

import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.metamodel.internal.BasicTypeImpl;
import org.hibernate.metamodel.internal.EntityTypeImpl;
import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.hibernate.query.criteria.internal.PathImplementor;
import org.hibernate.query.criteria.internal.path.AbstractPathImpl;
import org.hibernate.query.criteria.internal.path.MapAttributeJoin;
import org.hibernate.query.criteria.internal.path.SingularAttributePath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.*;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;
import javax.persistence.metamodel.Type.PersistenceType;
import java.lang.reflect.Member;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.Silent.class)
public class QueryByExampleFromSpecificationPredicateBuilderTest {

	@Mock
	private CriteriaBuilderImpl cb;

	@Mock
	private Root root;

	@Mock
	private EntityType<Person> personEntityType;

	@Before
	public void before() {
		given(root.getModel()).willReturn(personEntityType);

		SingularAttribute<Person, Integer> personIdAttribute =
				new DummySingularAttribute<>("id", Integer.class, PersistentAttributeType.BASIC);
		given(root.get(eq(personIdAttribute)))
				.willReturn(new DummySingularAttributePath<>(cb, Integer.class, personIdAttribute));

		SingularAttribute<Person, String> personNameAttribute =
				new DummySingularAttribute<>("name", String.class, PersistentAttributeType.BASIC);
		given(root.get(eq(personNameAttribute)))
				.willReturn(new SingularAttributePath<>(cb, String.class, null, personNameAttribute));

		given(personEntityType.getSingularAttributes())
				.willReturn(Stream.of(personIdAttribute, personNameAttribute).collect(Collectors.toSet()));

		PluralAttribute<Person, Set, String> personTagsAttribute =
				new DummyPluralAttribute<>(Person.class, Set.class, String.class, PersistenceType.BASIC,
						PersistentAttributeType.ELEMENT_COLLECTION, new DummyProperty("tags"));
		given(root.get(eq(personTagsAttribute))).willReturn(new DummyPluralAttributePath<>(cb, personTagsAttribute));

		PluralAttribute<Person, Map, String> personSkillsAttribute =
				new DummyPluralAttribute<>(Person.class, Map.class, String.class, PersistenceType.BASIC,
						PersistentAttributeType.ELEMENT_COLLECTION, new DummyProperty("skills"));
		given(root.get(eq(personSkillsAttribute))).willReturn(new DummyPluralAttributePath<>(cb, personSkillsAttribute));
		given(root.joinMap(eq("skills"))).willReturn(mock(MapAttributeJoin.class));

		given(personEntityType.getPluralAttributes())
				.willReturn(Stream.of(personTagsAttribute, personSkillsAttribute).collect(Collectors.toSet()));
	}

	@Test
	public void matchingAllShouldCreateTruePredicate() {
		Person person = new Person();
		Example<Person> example = Example.of(person);

		QueryByExampleFromSpecificationPredicateBuilder.getPredicate(root, cb, example);

		verify(cb, times(1)).isTrue(eq(cb.literal(eq(true))));
	}

	@Test
	public void ignoringAllShouldCreateTruePredicate() {
		Person person = new Person();
		person.setId(1);
		person.setName("Joe");
		person.setTags(new HashSet<>(Collections.singletonList("Friendly")));
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withIgnorePaths("id", "name", "tags");
		Example<Person> example = Example.of(person, exampleMatcher);

		QueryByExampleFromSpecificationPredicateBuilder.getPredicate(root, cb, example);

		verify(cb, times(1)).isTrue(eq(cb.literal(eq(true))));
	}

	@Test
	public void includeNullShouldCreateIsNullPredicate() {
		Person person = new Person();
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withIncludeNullValues();
		Example<Person> example = Example.of(person, exampleMatcher);

		QueryByExampleFromSpecificationPredicateBuilder.getPredicate(root, cb, example);

		verify(cb, times(2)).isNull(any(Expression.class));
		verify(cb, times(1)).and(any());
	}

	@Test
	public void nonStringShouldCreateEqualPredicate() {
		Person person = new Person();
		person.setId(1);
		Example<Person> example = Example.of(person);

		QueryByExampleFromSpecificationPredicateBuilder.getPredicate(root, cb, example);

		verify(cb, times(1)).equal(any(Expression.class), eq(person.getId()));
		verify(cb, times(1)).and(any());
	}

	@Test
	public void matchingIgnoreCaseWithStringShouldCreateEqualPredicate() {
		Person person = new Person();
		person.setName("Joe");
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withMatcher("name", GenericPropertyMatcher::ignoreCase);
		Example<Person> example = Example.of(person, exampleMatcher);

		QueryByExampleFromSpecificationPredicateBuilder.getPredicate(root, cb, example);

		verify(cb, times(1)).lower(any(Expression.class));
		verify(cb, times(1)).equal(isNull(), eq(person.getName().toLowerCase())); // null from cb.lower
		verify(cb, times(1)).and(any());
	}

	@Test
	public void matchingExactWithStringShouldCreateEqualPredicate() {
		Person person = new Person();
		person.setName("Joe");
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withMatcher("name", GenericPropertyMatcher::exact);
		Example<Person> example = Example.of(person, exampleMatcher);

		QueryByExampleFromSpecificationPredicateBuilder.getPredicate(root, cb, example);

		verify(cb, times(1)).equal(any(Expression.class), eq(person.getName()));
		verify(cb, times(1)).and(any());
	}

	@Test
	public void matchingContainingWithStringShouldCreateLikePredicate() {
		Person person = new Person();
		person.setName("Joe");
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withMatcher("name", GenericPropertyMatcher::contains);
		Example<Person> example = Example.of(person, exampleMatcher);

		QueryByExampleFromSpecificationPredicateBuilder.getPredicate(root, cb, example);

		verify(cb, times(1)).like(any(Expression.class), eq("%" + person.getName() + "%"));
		verify(cb, times(1)).and(any());
	}

	@Test
	public void matchingStartingWithStringShouldCreateLikePredicate() {
		Person person = new Person();
		person.setName("Joe");
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withMatcher("name", GenericPropertyMatcher::startsWith);
		Example<Person> example = Example.of(person, exampleMatcher);

		QueryByExampleFromSpecificationPredicateBuilder.getPredicate(root, cb, example);

		verify(cb, times(1)).like(any(Expression.class), eq(person.getName() + "%"));
		verify(cb, times(1)).and(any());
	}

	@Test
	public void matchingEndingWithStringShouldCreateLikePredicate() {
		Person person = new Person();
		person.setName("Joe");
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withMatcher("name", GenericPropertyMatcher::endsWith);
		Example<Person> example = Example.of(person, exampleMatcher);

		QueryByExampleFromSpecificationPredicateBuilder.getPredicate(root, cb, example);

		verify(cb, times(1)).like(any(Expression.class), eq("%" + person.getName()));
		verify(cb, times(1)).and(any());
	}

	@Test
	public void elementCollectionOfTypeCollectionShouldCreateIsMemberPredicate() {
		Person person = new Person();
		person.setTags(new HashSet<>(Collections.singletonList("Friendly")));
		Example<Person> example = Example.of(person);

		QueryByExampleFromSpecificationPredicateBuilder.getPredicate(root, cb, example);

		for (String tag : person.getTags()) {
			verify(cb, times(1)).isMember(eq(tag), any(Expression.class));
		}
		verify(cb, times(1)).and(any());
	}

	@Test
	public void elementCollectionOfTypeMapShouldCreateEqualPredicateFromMapJoin() {
		Person person = new Person();
		person.setSkills(Stream.of(new SimpleEntry<>("drawing", 8))
				.collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue)));
		Example<Person> example = Example.of(person);

		QueryByExampleFromSpecificationPredicateBuilder.getPredicate(root, cb, example);

		for (Entry<String, Integer> entry : person.getSkills().entrySet()) {
			verify(cb, times(1)).equal(isNull(), eq(entry.getValue())); // mapJoin.value()
			verify(cb, times(1)).equal(isNull(), eq(entry.getKey())); // mapJoin.key()
		}

		verify(cb, times(1)).and(any());
	}

	class Person {

		private Integer id;
		private String name;
		private Set<String> tags;
		private Map<String, Integer> skills;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Set<String> getTags() {
			return tags;
		}

		public void setTags(Set<String> tags) {
			this.tags = tags;
		}

		public Map<String, Integer> getSkills() {
			return skills;
		}

		public void setSkills(Map<String, Integer> skills) {
			this.skills = skills;
		}

	}

	private class DummySingularAttribute<X, T> implements SingularAttribute<X, T> {

		private String name;
		private Class<T> javaType;
		private PersistentAttributeType attributeType;

		DummySingularAttribute(String name, Class<T> javaType, PersistentAttributeType attributeType) {
			this.name = name;
			this.javaType = javaType;
			this.attributeType = attributeType;
		}

		@Override
		public boolean isId() {
			return "id".equals(name);
		}

		@Override
		public boolean isVersion() {
			return false;
		}

		@Override
		public boolean isOptional() {
			return false;
		}

		@Override
		public Type<T> getType() {
			return null;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public PersistentAttributeType getPersistentAttributeType() {
			return attributeType;
		}

		@Override
		public ManagedType<X> getDeclaringType() {
			return null;
		}

		@Override
		public Class<T> getJavaType() {
			return javaType;
		}

		@Override
		public Member getJavaMember() {
			return null;
		}

		@Override
		public boolean isAssociation() {
			return false;
		}

		@Override
		public boolean isCollection() {
			return false;
		}

		@Override
		public BindableType getBindableType() {
			return null;
		}

		@Override
		public Class<T> getBindableJavaType() {
			return null;
		}

	}

	private class DummySingularAttributePath<X> extends AbstractPathImpl<X> {

		private SingularAttribute<?, X> attribute;

		DummySingularAttributePath(CriteriaBuilderImpl criteriaBuilder, Class<X> javaType,
				SingularAttribute<?, X> attribute) {
			super(criteriaBuilder, javaType, null);
			this.attribute = attribute;
		}

		@Override
		protected boolean canBeDereferenced() {
			return false;
		}

		@Override
		protected Attribute locateAttributeInternal(String attributeName) {
			return null;
		}

		@Override
		public SingularAttribute<?, X> getAttribute() {
			return attribute;
		}

		@Override
		public <T extends X> PathImplementor<T> treatAs(Class<T> treatAsType) {
			return null;
		}

		@Override
		public Bindable<X> getModel() {
			return getAttribute();
		}

	}

	private class DummyPluralAttribute<X, C, E> implements PluralAttribute<X, C, E> {

		private Class<X> ownerType;
		private Class<C> collectionClass;
		private Class<E> elementType;
		private PersistenceType persistenceType;
		private PersistentAttributeType attributeType;
		private Property property;
		private PersistentClass persistentClass;

		DummyPluralAttribute(Class<X> ownerType, Class<C> collectionClass, Class<E> elementType,
				PersistenceType persistenceType, PersistentAttributeType attributeType, Property property) {
			this.ownerType = ownerType;
			this.collectionClass = collectionClass;
			this.elementType = elementType;
			this.persistenceType = persistenceType;
			this.attributeType = attributeType;
			this.property = property;
			this.persistentClass = mock(PersistentClass.class);
		}

		@Override
		public CollectionType getCollectionType() {
			if (Map.class.equals(collectionClass)) {
				return CollectionType.MAP;
			} else if (Set.class.equals(collectionClass)) {
				return CollectionType.SET;
			} else if (List.class.equals(collectionClass)) {
				return CollectionType.LIST;
			} else if (Collection.class.equals(collectionClass)) {
				return CollectionType.COLLECTION;
			}
			return null;
		}

		@Override
		public Type<E> getElementType() {
			return new BasicTypeImpl<>(elementType, persistenceType);
		}

		@Override
		public String getName() {
			return property.getName();
		}

		@Override
		public PersistentAttributeType getPersistentAttributeType() {
			return attributeType;
		}

		@Override
		public ManagedType<X> getDeclaringType() {
			return new EntityTypeImpl<>(ownerType, null, persistentClass);
		}

		@Override
		public Class<C> getJavaType() {
			return collectionClass;
		}

		@Override
		public Member getJavaMember() {
			return null;
		}

		@Override
		public boolean isAssociation() {
			return false;
		}

		@Override
		public boolean isCollection() {
			return false;
		}

		@Override
		public BindableType getBindableType() {
			return BindableType.PLURAL_ATTRIBUTE;
		}

		@Override
		public Class<E> getBindableJavaType() {
			return null;
		}

	}

	private class DummyPluralAttributePath<X> extends AbstractPathImpl<X> {

		private PluralAttribute<?, X, ?> attribute;

		DummyPluralAttributePath(CriteriaBuilderImpl criteriaBuilder, PluralAttribute<?, X, ?> attribute) {
			super(criteriaBuilder, attribute.getJavaType(), null);
			this.attribute = attribute;
		}

		@Override
		protected boolean canBeDereferenced() {
			return false;
		}

		@Override
		protected Attribute locateAttributeInternal(String attributeName) {
			return null;
		}

		@Override
		public PluralAttribute<?, X, ?> getAttribute() {
			return attribute;
		}

		@Override
		public <T extends X> PathImplementor<T> treatAs(Class<T> treatAsType) {
			return null;
		}

		@Override
		public Bindable<X> getModel() {
			return null;
		}

	}

	private class DummyProperty extends Property {

		private String name;

		DummyProperty(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}

	}

}