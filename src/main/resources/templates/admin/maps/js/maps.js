$(function() {
    var patch = /*[[${latestRiotPatch}]]*/ '7.12.1';
    // Maps difference
    var differenceDataTable = $('#maps-difference').DataTable({
        order: [ [1, 'asc'] ],
        ajax: {
            url: /*[[@{/admin/maps/diff}]]*/ '/admin/maps/diff',
            dataSrc: ''
        },
        columnDefs: [
            { orderable: false, targets: [0, 2] },
            { searchable: false, targets: [0, 2] }
        ],
        columns: [
            { data: null,
                render: function(data, type, full, meta) {
                    return $('<img>', {
                        'class': 'ui rounded image',
                        style: 'height:60px;width:auto;',
                        src: 'https://ddragon.leagueoflegends.com/cdn/' + patch + '/img/map/' + data.image.full
                    }).wrap('<span>').parent().html();
                }
            },
            { data: 'mapName' },
            { data: null,
                render: function(data, type, full, meta) {
                    var button = $('<button>', {
                        'data-map-id': data.mapId,
                        'class': 'ui fluid primary button',
                        style: 'box-sizing:border-box;',
                        text: /*[[#{admin.map.save}]]*/ 'admin.map.save'
                    });
                    return button.wrap('<span>').parent().html();
                }
            }
        ],
        lengthMenu: [ [10, -1], [10, 'All'] ],
        language: {
            emptyTable: /*[[#{dataTables.emptyTable(#{admin.maps})}]]*/ 'dataTables.emptyTable',
            info: /*[[#{dataTables.info}]]*/ 'dataTables.info',
            infoEmpty: /*[[#{dataTables.infoEmpty}]]*/ 'dataTables.infoEmpty',
            infoFiltered: /*[[#{dataTables.infoFiltered}]]*/ 'dataTables.infoFiltered',
            processing: /*[[#{dataTables.processing}]]*/ 'dataTables.processing',
            search: /*[[#{dataTables.search}]]*/ 'dataTables.search',
            zeroRecords: /*[[#{dataTables.zeroRecords(#{admin.maps})}]]*/ 'dataTables.zeroRecords',
            paginate: {
                next: /*[[#{dataTables.paginate.next}]]*/ 'dataTables.paginate.next',
                previous: /*[[#{dataTables.paginate.previous}]]*/ 'dataTables.paginate.previous'
            }
        }
    });
    $('.maps-difference-search-input').keyup(function() {
        differenceDataTable.column($(this).data('column-index')).search($(this).val()).draw();
    });
    $('#maps-difference tbody').on('click', '.primary', function(event) {
        $.ajax({
            type: 'POST',
            url: (/*[[@{/riot/maps/}]]*/ '/riot/maps/') + $(this).data('map-id'),
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
                $('#maps-difference-error-msg').removeClass('visible').addClass('hidden');
                differenceDataTable.ajax.reload();
                dataTable.ajax.reload();
            },
            error: function() {
                $(event.target).removeClass('loading');
                $('#maps-difference-error-msg .header').text(/*[[#{admin.map.save.error}]]*/ 'admin.map.save.error');
                $('#maps-difference-error-msg').removeClass('hidden').addClass('visible');
            },
            complete: function() {
                $(event.target).removeClass('loading');
            }
        });
    });
    $('#save-maps-form').submit(function(event) {
        $.ajax({
            type: 'POST',
            url: /*[[@{/riot/maps}]]*/ '/riot/maps',
            beforeSend: function() {
                $('#save-maps-btn').addClass('loading');
            },
            data: {
                truncate: $('#truncate').is(':checked'),
                _csrf: /*[[${_csrf.token}]]*/ '_csrf.token'
            },
            headers: {
                _csrf_header: /*[[${_csrf.headerName}]]*/ '_csrf.headerName'
            },
            success: function(data) {
                $('#maps-difference-error-msg').removeClass('visible').addClass('hidden');
                differenceDataTable.ajax.reload();
                dataTable.ajax.reload();
            },
            error: function() {
                $('#save-maps-btn').removeClass('loading');
                $('#maps-difference-error-msg .header').text(/*[[#{admin.maps.save.error}]]*/ 'admin.maps.save.error');
                $('#maps-difference-error-msg').removeClass('hidden').addClass('visible');
            },
            complete: function() {
                $('#save-maps-btn').removeClass('loading');
            }
        });
        event.preventDefault();
    });
    differenceDataTable.on('xhr', function() {
        if ($.isEmptyObject(differenceDataTable.ajax.json())) {
            $('#maps-difference-segment').slideUp();
			$('#maps-difference-none').removeClass('hidden')
		} else {
            $('#maps-difference-segment').slideDown();
			$('#maps-difference-none').addClass('hidden');
		}
		$('#maps-difference-loading').addClass('hidden');
	});
    // Maps from the db
    var dataTable = $('#maps').DataTable({
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
				var searches = JSON.parse(data.search.value);
				parameters[searches.column] = searches.values;
            }
            parameters['page'] = Math.ceil(data.start / data.length);
            parameters['size'] = data.length;
            parameters['sort'] = sorts;
            $.get(/*[[@{/api/maps}]]*/ '/api/maps', $.param(parameters, true), function(json) {
                    callback({ draw: data.draw, recordsTotal: json.page.totalElements,
                        recordsFiltered: json.page.totalElements,
                        data: json._embedded === undefined ? {} : json._embedded.maps
                    });
                }
            );
        },
        columnDefs: [
            { orderable: false, targets: [0, 2, 3] }
        ],
        columns: [
            { data: 'image.full',
                render: function(data, type, full, meta) {
                    return $('<img>', {
                        'class': 'ui rounded image',
                        style: 'height:60px;width:auto;',
                        src: '/img/maps/' + data
                    }).wrap('<span>').parent().html();
                }
            },
            { data: 'mapName' },
            { data: '_links.self.href',
                render: function(data, type, full, meta) {
                    var button = $('<button>', {
                        'data-map-id': data.substring(data.lastIndexOf('/') + 1, data.length),
                        'class': 'ui secondary button',
                        text: /*[[#{admin.map.overwrite}]]*/ 'admin.map.overwrite'
                    });
                    return button.wrap('<span>').parent().html();
                }
            },
            { data: '_links.self.href',
                render: function(data, type, full, meta) {
                    var button = $('<button>', {
                        'data-map-id': data.substring(data.lastIndexOf('/') + 1, data.length),
                        'class': 'negative ui button',
                        text: /*[[#{admin.map.delete}]]*/ 'admin.map.delete'
                    });
                    return button.wrap('<span>').parent().html();
                }
            }
        ],
        lengthMenu: [ [10, 1000], [10, 'All'] ],
        language: {
            emptyTable: /*[[#{dataTables.emptyTable(#{admin.maps})}]]*/ 'dataTables.emptyTable',
            info: /*[[#{dataTables.info}]]*/ 'dataTables.info',
            infoEmpty: /*[[#{dataTables.infoEmpty}]]*/ 'dataTables.infoEmpty',
            infoFiltered: /*[[#{dataTables.infoFiltered}]]*/ 'dataTables.infoFiltered',
            processing: /*[[#{dataTables.processing}]]*/ 'dataTables.processing',
            search: /*[[#{dataTables.search}]]*/ 'dataTables.search',
            zeroRecords: /*[[#{dataTables.zeroRecords(#{admin.maps})}]]*/ 'dataTables.zeroRecords',
            paginate: {
                next: /*[[#{dataTables.paginate.next}]]*/ 'dataTables.paginate.next',
                previous: /*[[#{dataTables.paginate.previous}]]*/ 'dataTables.paginate.previous'
            }
        }
    });
    $('.maps-search-input').keyup(function() {
        var inputs = [];
        $('.maps-search-input').each(function() {
            if ($(this).val()) {
				inputs.push(JSON.stringify({
					column: $(this).data('column-name'),
					values: $(this).val()
				}));
            }
        });
        dataTable.search(inputs).draw();
    });
    $('#maps tbody').on('click', '.secondary', function(event) {
        $.ajax({
            type: 'POST',
            url: (/*[[@{/riot/maps/}]]*/ '/riot/maps/') + $(this).data('map-id'),
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
                $('#maps-error-msg').removeClass('visible').addClass('hidden');
                dataTable.ajax.reload();
            },
            error: function() {
                $(event.target).removeClass('loading');
                $('#maps-error-msg .header').text(/*[[#{admin.map.overwrite.error}]]*/ 'admin.map.overwrite.error');
                $('#maps-error-msg').removeClass('hidden').addClass('visible');
            },
            complete: function() {
                $(event.target).removeClass('loading');
            }
        });
    });
    $('#maps tbody').on('click', '.negative', function(event) {
        $.ajax({
            type: 'DELETE',
            url: (/*[[@{/api/maps/}]]*/ '/api/maps/') + $(this).data('map-id'),
            beforeSend: function() {
                $(event.target).addClass('loading');
            },
            headers: {
                'X-CSRF-TOKEN': /*[[${_csrf.token}]]*/ '_csrf.token'
            },
            success: function(data) {
                $('#maps-error-msg').removeClass('visible').addClass('hidden');
                differenceDataTable.ajax.reload();
                dataTable.ajax.reload();
            },
            error: function() {
                $(event.target).removeClass('loading');
                $('#maps-error-msg .header').text(/*[[#{admin.map.delete.error}]]*/ 'admin.map.delete.error');
                $('#maps-error-msg').removeClass('hidden').addClass('visible');
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
