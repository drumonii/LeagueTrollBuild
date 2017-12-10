$(window).on('load', function() {
    $('.cards .image').dimmer({
        on: 'hover'
    });
    var regex,
        tagFilter,
        isotope;

    isotope = $('#champions-cards').isotope({
        itemSelector: '.card',
        category: '[data-champion-tags]',
        transitionDuration: 0,
        filter: function() {
            var searchResult = regex ? $(this).text().match(regex) : true;
            var filterResult;
            if (tagFilter === '*') {
                filterResult = true;
            } else {
                filterResult = tagFilter ? $.inArray(tagFilter, $(this).find('.content').data('champion-tags')
                        .replace('[', '').replace(']', '').split(', ')) > -1 : true;
            }
            return searchResult && filterResult;
        }
    });

    $('#champions-search-input').keyup(function() {
        regex = new RegExp($(this).val(), 'gi');
        isotope.isotope();
    });
    $('#champion-tags-filter-button-group').on('click', '.button', function() {
        $('#champions-search-input').val(''); regex = '';
        $('#champion-tags-filter-button-group').find('.active').removeClass('active');
        $(this).addClass('active');
        tagFilter = $(this).data('champion-tag-filter');
        isotope.isotope();
    });
});