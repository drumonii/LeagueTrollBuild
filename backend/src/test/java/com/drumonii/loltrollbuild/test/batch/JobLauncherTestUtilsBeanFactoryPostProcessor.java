package com.drumonii.loltrollbuild.test.batch;

import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.AutowireCandidateQualifier;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

public class JobLauncherTestUtilsBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;

        String[] beanDefinitionNames = defaultListableBeanFactory.getBeanDefinitionNames();

        List<String> jobBeanDefinitionNames = Arrays.stream(beanDefinitionNames)
                .filter(beanDefinitionName -> beanDefinitionName.endsWith("Job"))
                .collect(Collectors.toList());

        for (String jobBeanDefinitionName : jobBeanDefinitionNames) {
            String jobLauncherTestUtilsBeanName = jobBeanDefinitionName.substring(0, jobBeanDefinitionName.lastIndexOf("J"))
                    + "JobLauncherTestUtils";

            AutowireCandidateQualifier batchJobAutowireCandidateQualifier =
                    new AutowireCandidateQualifier(Qualifier.class, jobBeanDefinitionName);

            AutowireCandidateQualifier jobLauncherTestUtilsAutowireCandidateQualifier =
                    new AutowireCandidateQualifier(Qualifier.class, jobLauncherTestUtilsBeanName);

            AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.rootBeanDefinition(JobLauncherTestUtils.class)
                    .setScope(SCOPE_SINGLETON)
                    .addPropertyReference("job", jobBeanDefinitionName)
                    .getBeanDefinition();

            beanDefinition.addQualifier(batchJobAutowireCandidateQualifier);
            beanDefinition.addQualifier(jobLauncherTestUtilsAutowireCandidateQualifier);

            defaultListableBeanFactory.registerBeanDefinition(jobLauncherTestUtilsBeanName, beanDefinition);
        }
    }

}
