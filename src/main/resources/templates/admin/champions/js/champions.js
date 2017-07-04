$(function() {
    var patch = /*[[${latestRiotPatch}]]*/ '7.12.1';
    // Champions difference
    var differenceDataTable = $('#champions-difference').DataTable({
        processing: true,
        order: [ [1, 'asc'] ],
        ajax: {
            url: /*[[@{/admin/champions/diff}]]*/ '/admin/champions/diff',
            dataSrc: ''
        },
        columnDefs: [
            { orderable: false, targets: [0, 3, 5] },
            { searchable: false, targets: [0, 5] }
        ],
        columns: [
            { data: null,
                render: function(data, type, full, meta) {
                    return $('<img>', {
                        'class': 'ui rounded image',
                        style: 'height:60px;width:auto;',
                        src: 'https://ddragon.leagueoflegends.com/cdn/' + patch + '/img/champion/' + data.image.full
                    }).wrap('<span>').parent().html();
                }
            },
            { data: 'name' },
            { data: 'title' },
            { data: 'tags[, ]' },
            { data: 'partype' },
            { data: null,
                render: function(data, type, full, meta) {
                    var button = $('<button>', {
                        'data-champion-id': data.id,
                        'class': 'ui fluid primary button',
                        style: 'box-sizing:border-box;',
                        text: /*[[#{admin.champion.save}]]*/ 'admin.champion.save'
                    });
                    return button.wrap('<span>').parent().html();
                }
            }
        ],
        lengthMenu: [ [10, 25, 50, 100, -1], [10, 25, 50, 100, 'All'] ],
        language: {
            emptyTable: /*[[#{dataTables.emptyTable(#{admin.champions})}]]*/ 'dataTables.emptyTable',
            info: /*[[#{dataTables.info}]]*/ 'dataTables.info',
            infoEmpty: /*[[#{dataTables.infoEmpty}]]*/ 'dataTables.infoEmpty',
            infoFiltered: /*[[#{dataTables.infoFiltered}]]*/ 'dataTables.infoFiltered',
            processing: /*[[#{dataTables.processing}]]*/ 'dataTables.processing',
            search: /*[[#{dataTables.search}]]*/ 'dataTables.search',
            zeroRecords: /*[[#{dataTables.zeroRecords(#{admin.champions})}]]*/ 'dataTables.zeroRecords',
            paginate: {
                next: /*[[#{dataTables.paginate.next}]]*/ 'dataTables.paginate.next',
                previous: /*[[#{dataTables.paginate.previous}]]*/ 'dataTables.paginate.previous'
            }
        }
    });
    $('.champions-different-search-input').keyup(function() {
        differenceDataTable.column($(this).data('column-index')).search($(this).val()).draw();
    });
    $('#champions-difference tbody').on('click', '.primary', function(event) {
        $.ajax({
            type: 'POST',
            url: (/*[[@{/riot/champions/}]]*/ '/riot/champions/') + $(this).data('champion-id'),
            beforeSend: function() {
                $(event.target).addClass('loading');
            },
            data: {
                _csrf: /*[[${_csrf.token}]]*/ '_csrf.token'
            },
            headers: {
                _csrf_header: /*[[${_csrf.headerName}]]*/ '_csrf.headerName'
            },
            success: function(data) {
                $('#champions-difference-error-msg').removeClass('visible').addClass('hidden');
                differenceDataTable.ajax.reload();
                dataTable.ajax.reload();
            },
            error: function() {
                $(event.target).removeClass('loading');
                $('#champions-difference-error-msg .header').text(/*[[#{admin.champion.save.error}]]*/ 'admin.champion.save.error');
                $('#champions-difference-error-msg').removeClass('hidden').addClass('visible');
            },
            complete: function() {
               $(event.target).removeClass('loading');
            }
        });
    });
    $('#save-champions-form').submit(function(event) {
        $.ajax({
            type: 'POST',
            url: /*[[@{/riot/champions}]]*/ '/riot/champions',
            beforeSend: function() {
                $('#save-champions-btn').addClass('loading');
            },
            data: { 
                truncate: $('#truncate').is(':checked'),
                _csrf: /*[[${_csrf.token}]]*/ '_csrf.token'
            },
            headers: {
                _csrf_header: /*[[${_csrf.headerName}]]*/ '_csrf.headerName'
            },
            success: function(data) {
                $('#champions-difference-error-msg').removeClass('visible').addClass('hidden');
                differenceDataTable.ajax.reload();
                dataTable.ajax.reload();
            },
            error: function() {
                $('#save-champions-btn').removeClass('loading');
                $('#champions-difference-error-msg .header').text(/*[[#{admin.champions.save.error}]]*/ 'admin.champions.save.error');
                $('#champions-difference-error-msg').removeClass('hidden').addClass('visible');
            },
            complete: function() {
                $('#save-champions-btn').removeClass('loading');
            }
        });
        event.preventDefault();
    });
    differenceDataTable.on('xhr', function() {
        if ($.isEmptyObject(differenceDataTable.ajax.json())) {
            $('#champions-difference-segment').slideUp();
        } else {
            $('#champions-difference-segment').slideDown();
        }
    });
    // Champions from the db
    var dataTable = $('#champions').DataTable({
        processing: true,
        serverSide: true,
        order: [ [1, 'asc'] ],
        ajax: function(data, callback, settings) {
            var sorts = [];
            $.each(data.order, function (i, order) {
                if (data.columns[order.column].orderable) {
                    sorts.push(data.columns[order.column].data + ',' + order.dir);
                }
            });
            var parameters = {};
            if (data.search.value) {
                var searches = data.search.value.split(',');
                $.each(searches, function(index, value) {
                    var search = value.split('|');
                    parameters[search[0]] = search[1];
                });
            }
            parameters['page'] = Math.ceil(data.start / data.length);
            parameters['size'] = data.length;
            parameters['sort'] = sorts;
            $.get(/*[[@{/api/champions}]]*/ '/api/champions', $.param(parameters, true), function(json) {
                    callback({ draw: data.draw, recordsTotal: json.page.totalElements,
                        recordsFiltered: json.page.totalElements,
                        data: json._embedded === undefined ? {} : json._embedded.champions
                    });
                }
            );
        },
        columnDefs: [
            { orderable: false, targets: [0, 3, 5, 6] }
        ],
        columns: [
            { data: null,
                render: function(data, type, full, meta) {
                    var href = data._links.self.href;
                    var id = href.substring(href.lastIndexOf('/') + 1, href.length);
                    var img = data.image.full;
                    return $('<img>', {
                        'class': 'ui rounded image',
                        style: 'height:60px;width:auto;',
                        src: '/img/champions/' + id + img.substring(img.lastIndexOf('.'), img.length)
                    }).wrap('<span>').parent().html();
                }
            },
            { data: 'name' },
            { data: 'title' },
            { data: 'tags[, ]' },
            { data: 'partype' },
            { data: '_links.self.href',
                render: function(data, type, full, meta) {
                    var button = $('<button>', {
                        'data-champion-id': data.substring(data.lastIndexOf('/') + 1, data.length),
                        'class': 'ui secondary button',
                        text: /*[[#{admin.champion.overwrite}]]*/ 'admin.champion.overwrite'
                    });
                    return button.wrap('<span>').parent().html();
                }
            },
            { data: '_links.self.href',
                render: function(data, type, full, meta) {
                    var button = $('<button>', {
                        'data-champion-id': data.substring(data.lastIndexOf('/') + 1, data.length),
                        'class': 'negative ui button',
                        text: /*[[#{admin.champion.delete}]]*/ 'admin.champion.delete'
                    });
                    return button.wrap('<span>').parent().html();
                }
            }
        ],
        lengthMenu: [ [10, 25, 50, 100, 1000], [10, 25, 50, 100, 'All'] ],
        language: {
            emptyTable: /*[[#{dataTables.emptyTable(#{admin.champions})}]]*/ 'dataTables.emptyTable',
            info: /*[[#{dataTables.info}]]*/ 'dataTables.info',
            infoEmpty: /*[[#{dataTables.infoEmpty}]]*/ 'dataTables.infoEmpty',
            infoFiltered: /*[[#{dataTables.infoFiltered}]]*/ 'dataTables.infoFiltered',
            processing: /*[[#{dataTables.processing}]]*/ 'dataTables.processing',
            search: /*[[#{dataTables.search}]]*/ 'dataTables.search',
            zeroRecords: /*[[#{dataTables.zeroRecords(#{admin.champions})}]]*/ 'dataTables.zeroRecords',
            paginate: {
                next: /*[[#{dataTables.paginate.next}]]*/ 'dataTables.paginate.next',
                previous: /*[[#{dataTables.paginate.previous}]]*/ 'dataTables.paginate.previous'
            }
        }
    });
    $('.champions-search-input').keyup(function() {
        var inputs = [];
        $('.champions-search-input').each(function() {
            if ($(this).val()) {
                inputs.push($(this).data('column-name') + '|' + $(this).val());
            }
        });
        dataTable.search(inputs).draw();
    });
    $('#champions tbody').on('click', '.secondary', function(event) {
        $.ajax({
            type: 'POST',
            url: (/*[[@{/riot/champions/}]]*/ '/riot/champions/') + $(this).data('champion-id'),
            beforeSend: function() {
                $(event.target).addClass('loading');
            },
            data: {
                _csrf: /*[[${_csrf.token}]]*/ '_csrf.token'
            },
            headers: {
                _csrf_header: /*[[${_csrf.headerName}]]*/ '_csrf.headerName'
            },
            success: function(data) {
                $('#champions-error-msg').removeClass('visible').addClass('hidden');
                dataTable.ajax.reload();
            },
            error: function() {
                $(event.target).removeClass('loading');
                $('#champions-error-msg .header').text(/*[[#{admin.champion.overwrite.error}]]*/ 'admin.champion.overwrite.error');
                $('#champions-error-msg').removeClass('hidden').addClass('visible');
            },
            complete: function() {
                $(event.target).removeClass('loading');
            }
        });
    });
    $('#champions tbody').on('click', '.negative', function(event) {
        $.ajax({
            type: 'DELETE',
            url: (/*[[@{/api/champions/}]]*/ 'api/champions/') + $(this).data('champion-id'),
            beforeSend: function() {
                $(event.target).addClass('loading');
            },
            headers: {
                'X-CSRF-TOKEN': /*[[${_csrf.token}]]*/ '_csrf.token'
            },
            success: function(data) {
                $('#champions-error-msg').removeClass('visible').addClass('hidden');
                differenceDataTable.ajax.reload();
                dataTable.ajax.reload();
            },
            error: function() {
                $(event.target).removeClass('loading');
                $('#champions-error-msg .header').text(/*[[#{admin.champion.delete.error}]]*/ 'admin.champion.delete.error');
                $('#champions-error-msg').removeClass('hidden').addClass('visible');
            },
            complete: function() {
                $(event.target).removeClass('loading');
            }
        });
    });
    $('.message').click(function() {
        $(this).transition('fade');
    });
});
