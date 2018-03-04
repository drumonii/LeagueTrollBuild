$(function() {
    var patch = /*[[${latestRiotPatch}]]*/ '7.12.1';
    // Summoner Spells difference
    var differenceDataTable = $('#summoner-spells-difference').DataTable({
        order: [ [1, 'asc'] ],
        ajax: {
            url: /*[[@{/admin/summoner-spells/diff}]]*/ '/admin/summoner-spells/diff',
            dataSrc: ''
        },
        columnDefs: [
            { orderable: false, targets: [0, 2, 3] },
            { searchable: false, targets: [0, 3] }
        ],
        columns: [
            { data: null,
                render: function(data, type, full, meta) {
                    return $('<img>', {
                        'class': 'ui rounded image',
                        style: 'height:60px;width:auto;',
                        src: 'https://ddragon.leagueoflegends.com/cdn/' + patch + '/img/spell/' + data.image.full
                    }).wrap('<span>').parent().html();
                }
            },
            { data: 'name' },
            { data: 'modes[, ]' },
            { data: null,
                render: function(data, type, full, meta) {
                    var button = $('<button>', {
                        'data-summoner-spell-id': data.id,
                        'class': 'ui fluid primary button',
                        style: 'box-sizing:border-box;',
                        text: /*[[#{admin.summonerSpell.save}]]*/ 'admin.summonerSpell.save'
                    });
                    return button.wrap('<span>').parent().html();
                }
            }
        ],
        lengthMenu: [ [10, -1], [10, 'All'] ],
        language: {
            emptyTable: /*[[#{dataTables.emptyTable(#{admin.summonerSpells})}]]*/ 'dataTables.emptyTable',
            info: /*[[#{dataTables.info}]]*/ 'dataTables.info',
            infoEmpty: /*[[#{dataTables.infoEmpty}]]*/ 'dataTables.infoEmpty',
            infoFiltered: /*[[#{dataTables.infoFiltered}]]*/ 'dataTables.infoFiltered',
            processing: /*[[#{dataTables.processing}]]*/ 'dataTables.processing',
            search: /*[[#{dataTables.search}]]*/ 'dataTables.search',
            zeroRecords: /*[[#{dataTables.zeroRecords(#{admin.summonerSpells})}]]*/ 'dataTables.zeroRecords',
            paginate: {
                next: /*[[#{dataTables.paginate.next}]]*/ 'dataTables.paginate.next',
                previous: /*[[#{dataTables.paginate.previous}]]*/ 'dataTables.paginate.previous'
            }
        },
        drawCallback: function(settings) {
            $('.summoner-spell-description').popup();
        }
    });
    $('.summoner-spells-difference-search-input').keyup(function() {
        differenceDataTable.column($(this).data('column-index')).search($(this).val()).draw();
    });
    $('#summoner-spells-difference tbody').on('click', '.primary', function(event) {
        $.ajax({
            type: 'POST',
            url: (/*[[@{/riot/summoner-spells/}]]*/ '/riot/summoner-spells/') + $(this).data('summoner-spell-id'),
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
                $('#summoner-spells-difference-error-msg').removeClass('visible').addClass('hidden');
                differenceDataTable.ajax.reload();
                dataTable.ajax.reload();
            },
            error: function() {
                $(event.target).removeClass('loading');
                $('#summoner-spells-difference-error-msg .header').text(/*[[#{admin.summonerSpell.save.error}]]*/ 'admin.summonerSpell.save.error');
                $('#summoner-spells-difference-error-msg').removeClass('hidden').addClass('visible');
            },
            complete: function() {
                $(event.target).removeClass('loading');
            }
        });
    });
    $('#save-summoner-spells-form').submit(function(event) {
        $.ajax({
            type: 'POST',
            url: /*[[@{/riot/summoner-spells}]]*/ '/riot/summoner-spells',
            beforeSend: function() {
                $('#save-summoner-spells-btn').addClass('loading');
            },
            data: {
                truncate: $('#truncate').is(':checked'),
                _csrf: /*[[${_csrf.token}]]*/ '_csrf.token'
            },
            headers: {
                _csrf_header: /*[[${_csrf.headerName}]]*/ '_csrf.headerName'
            },
            success: function(data) {
                $('#summoner-spells-difference-error-msg').removeClass('visible').addClass('hidden');
                differenceDataTable.ajax.reload();
                dataTable.ajax.reload();
            },
            error: function() {
                $('#save-summoner-spells-btn').removeClass('loading');
                $('#summoner-spells-difference-error-msg .header').text(/*[[#{admin.summonerSpells.save.error}]]*/ 'admin.summonerSpells.save.error');
                $('#summoner-spells-difference-error-msg').removeClass('hidden').addClass('visible');
            },
            complete: function() {
                $('#save-summoner-spells-btn').removeClass('loading');
            }
        });
        event.preventDefault();
    });
    differenceDataTable.on('xhr', function() {
        if ($.isEmptyObject(differenceDataTable.ajax.json())) {
            $('#summoner-spells-difference-segment').slideUp();
			$('#summoner-spells-difference-none').removeClass('hidden')
		} else {
            $('#summoner-spells-difference-segment').slideDown();
            $('#summoner-spells-difference-none').addClass('hidden');
        }
		$('#summoner-spells-difference-loading').addClass('hidden');
	});
    // Summoner Spells from the db
    var dataTable = $('#summoner-spells').DataTable({
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
            $.get(/*[[@{/api/summoner-spells}]]*/ '/api/summoner-spells', $.param(parameters, true), function(json) {
                    callback({ draw: data.draw, recordsTotal: json.page.totalElements,
                        recordsFiltered: json.page.totalElements,
                        data: json._embedded === undefined ? {} : json._embedded.summonerSpells
                    });
                }
            );
        },
        columnDefs: [
            { orderable: false, targets: [0, 2, 3, 4] }
        ],
        columns: [
            { data: null,
                render: function(data, type, full, meta) {
                    var href = data._links.self.href;
                    var id = href.substring(href.lastIndexOf('/') + 1, href.length);
                    var img = data.image.full;
                    return $('<div>', {
                        'class': 'summoner-spell-description',
                        'data-html': data.description,
                        'data-variation': 'wide'
                    }).append($('<img>', {
                        'class': 'ui rounded image',
                        src: '/img/summoner-spells/' + id + img.substring(img.lastIndexOf('.'), img.length)
                    })).wrap('<span>').parent().html();
                }
            },
            { data: 'name' },
            { data: 'modes[, ]' },
            { data: '_links.self.href',
                render: function(data, type, full, meta) {
                    var button = $('<button>', {
                        'data-summoner-spell-id': data.substring(data.lastIndexOf('/') + 1, data.length),
                        'class': 'ui secondary button',
                        text: /*[[#{admin.summonerSpell.overwrite}]]*/ 'admin.summonerSpell.overwrite'
                    });
                    return button.wrap('<span>').parent().html();
                }
            },
            { data: '_links.self.href',
                render: function(data, type, full, meta) {
                    var button = $('<button>', {
                        'data-summoner-spell-id': data.substring(data.lastIndexOf('/') + 1, data.length),
                        'class': 'negative ui button',
                        text: /*[[#{admin.summonerSpell.delete}]]*/ 'admin.summonerSpell.delete'
                    });
                    return button.wrap('<span>').parent().html();
                }
            }
        ],
        lengthMenu: [ [10, 1000], [10, 'All'] ],
        language: {
            emptyTable: /*[[#{dataTables.emptyTable(#{admin.summonerSpells})}]]*/ 'dataTables.emptyTable',
            info: /*[[#{dataTables.info}]]*/ 'dataTables.info',
            infoEmpty: /*[[#{dataTables.infoEmpty}]]*/ 'dataTables.infoEmpty',
            infoFiltered: /*[[#{dataTables.infoFiltered}]]*/ 'dataTables.infoFiltered',
            processing: /*[[#{dataTables.processing}]]*/ 'dataTables.processing',
            search: /*[[#{dataTables.search}]]*/ 'dataTables.search',
            zeroRecords: /*[[#{dataTables.zeroRecords(#{admin.summonerSpells})}]]*/ 'dataTables.zeroRecords',
            paginate: {
                next: /*[[#{dataTables.paginate.next}]]*/ 'dataTables.paginate.next',
                previous: /*[[#{dataTables.paginate.previous}]]*/ 'dataTables.paginate.previous'
            }
        },
        drawCallback: function(settings) {
            $('.summoner-spell-description').popup();
        }
    });
    $('.summoner-spells-search-input').keyup(function() {
        var inputs = [];
        $('.summoner-spells-search-input').each(function() {
            if ($(this).val()) {
                inputs.push($(this).data('column-name') + '|' + $(this).val());
            }
        });
        dataTable.search(inputs).draw();
    });
    $('#summoner-spells tbody').on('click', '.secondary', function(event) {
        $.ajax({
            type: 'POST',
            url: (/*[[@{/riot/summoner-spells/}]]*/ '/riot/summoner-spells/') + $(this).data('summoner-spell-id'),
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
                $('#summoner-spells-error-msg').removeClass('visible').addClass('hidden');
                dataTable.ajax.reload();
            },
            error: function() {
                $(event.target).removeClass('loading');
                $('#summoner-spells-error-msg .header').text(/*[[#{admin.summonerSpell.overwrite.error}]]*/ 'admin.summonerSpell.overwrite.error');
                $('#summoner-spells-error-msg').removeClass('hidden').addClass('visible');
            },
            complete: function() {
                $(event.target).removeClass('loading');
            }
        });
    });
    $('#summoner-spells tbody').on('click', '.negative', function(event) {
        $.ajax({
            type: 'DELETE',
            url: (/*[[@{/api/summoner-spells/}]]*/ '/api/summoner-spells/') + $(this).data('summoner-spell-id'),
            beforeSend: function() {
                $(event.target).addClass('loading');
            },
            headers: {
                'X-CSRF-TOKEN': /*[[${_csrf.token}]]*/ '_csrf.token'
            },
            success: function(data) {
                $('#summoner-spells-error-msg').removeClass('visible').addClass('hidden');
                differenceDataTable.ajax.reload();
                dataTable.ajax.reload();
            },
            error: function() {
                $(event.target).removeClass('loading');
                $('#summoner-spells-error-msg .header').text(/*[[#{admin.summonerSpell.delete.error}]]*/ 'admin.summonerSpell.delete.error');
                $('#summoner-spells-error-msg').removeClass('hidden').addClass('visible');
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
