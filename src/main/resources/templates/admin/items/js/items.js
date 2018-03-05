$(function() {
    var crystalScarId = /*[[${T(com.drumonii.loltrollbuild.util.GameMapUtil).CRYSTAL_SCAR_ID}]]*/ 8;
    var twistedTreelineId = /*[[${T(com.drumonii.loltrollbuild.util.GameMapUtil).TWISTED_TREELINE_ID}]]*/ 10;
    var summonersRiftId = /*[[${T(com.drumonii.loltrollbuild.util.GameMapUtil).SUMMONERS_RIFT_ID}]]*/ 11;
    var howlingAbyssId = /*[[${T(com.drumonii.loltrollbuild.util.GameMapUtil).HOWLING_ABYSS_ID}]]*/ 12;

	var crystalScar = /*[[${T(com.drumonii.loltrollbuild.util.GameMapUtil).CRYSTAL_SCAR}]]*/ 'Crystal Scar';
	var twistedTreeline = /*[[${T(com.drumonii.loltrollbuild.util.GameMapUtil).TWISTED_TREELINE}]]*/ 'Twisted Treeline';
	var summonersRift = /*[[${T(com.drumonii.loltrollbuild.util.GameMapUtil).SUMMONERS_RIFT}]]*/ 'Summoner\'s Rift';
	var howlingAbyss = /*[[${T(com.drumonii.loltrollbuild.util.GameMapUtil).HOWLING_ABYSS}]]*/ 'Howling Abyss';

    var patch = /*[[${latestRiotPatch}]]*/ '7.12.1';
    // Items difference
    var differenceDataTable = $('#items-difference').DataTable({
        order: [ [1, 'asc'] ],
        ajax: {
            url: /*[[@{/admin/items/diff}]]*/ '/admin/items/diff',
            dataSrc: ''
        },
        columnDefs: [
            { orderable: false, targets: [0, 4, 5] },
            { searchable: false, targets: [0, 4, 5] }
        ],
        columns: [
            { data: null,
                render: function(data, type, full, meta) {
                    return $('<img>', {
                        'class': 'ui rounded image',
                        style: 'height:60px;width:auto;',
                        src: 'https://ddragon.leagueoflegends.com/cdn/' + patch + '/img/item/' + data.image.full
                    }).wrap('<span>').parent().html();
                }
            },
            { data: 'name' },
            { data: 'group',
                render: function(data, type, full, meta) {
					return data ? data : /*[[#{none}]]*/ 'none';
                }
            },
            { data: 'requiredChampion',
                render: function(data, type, full, meta) {
					return data ? data : /*[[#{none}]]*/ 'none';
                }
            },
            { data: 'maps',
                render: function(data, type, full, meta) {
                    return getMaps(data);
                }
            },
            { data: null,
                render: function(data, type, full, meta) {
                    var button = $('<button>', {
                        'data-item-id': data.id,
                        'class': 'ui fluid primary button',
                        style: 'box-sizing:border-box;',
                        text: /*[[#{admin.item.save}]]*/ 'admin.item.save'
                    });
                    return button.wrap('<span>').parent().html();
                }
            }
        ],
        lengthMenu: [ [10, 25, 50, 100, -1], [10, 25, 50, 100, 'All'] ],
        language: {
            emptyTable: /*[[#{dataTables.emptyTable(#{admin.items})}]]*/ 'dataTables.emptyTable',
            info: /*[[#{dataTables.info}]]*/ 'dataTables.info',
            infoEmpty: /*[[#{dataTables.infoEmpty}]]*/ 'dataTables.infoEmpty',
            infoFiltered: /*[[#{dataTables.infoFiltered}]]*/ 'dataTables.infoFiltered',
            processing: /*[[#{dataTables.processing}]]*/ 'dataTables.processing',
            search: /*[[#{dataTables.search}]]*/ 'dataTables.search',
            zeroRecords: /*[[#{dataTables.zeroRecords(#{admin.items})}]]*/ 'dataTables.zeroRecords',
            paginate: {
                next: /*[[#{dataTables.paginate.next}]]*/ 'dataTables.paginate.next',
                previous: /*[[#{dataTables.paginate.previous}]]*/ 'dataTables.paginate.previous'
            }
        },
        drawCallback: function(settings) {
            $('.item-description').popup();
        }
    });
    $('.items-difference-search-input').keyup(function() {
        differenceDataTable.column($(this).data('column-index')).search($(this).val()).draw();
    });
    $('#items-difference tbody').on('click', '.primary', function(event) {
        $.ajax({
            type: 'POST',
            url: (/*[[@{/riot/items/}]]*/ '/riot/items/') + $(this).data('item-id'),
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
                $('#items-difference-error-msg').removeClass('visible').addClass('hidden');
                differenceDataTable.ajax.reload();
                dataTable.ajax.reload();
            },
            error: function() {
                $(event.target).removeClass('loading');
                $('#items-difference-error-msg .header').text(/*[[#{admin.item.save.error}]]*/ 'admin.item.save.error');
                $('#items-difference-error-msg').removeClass('hidden').addClass('visible');
            },
            complete: function() {
                $(event.target).removeClass('loading');
            }
        });
    });
    $('#save-items-form').submit(function(event) {
        $.ajax({
            type: 'POST',
            url: /*[[@{/riot/items}]]*/ '/riot/items',
            beforeSend: function() {
                $('#save-items-btn').addClass('loading');
            },
            data: {
                truncate: $('#truncate').is(':checked'),
                _csrf: /*[[${_csrf.token}]]*/ '_csrf.token'
            },
            headers: {
                _csrf_header: /*[[${_csrf.headerName}]]*/ '_csrf.headerName'
            },
            success: function(data) {
                $('#items-difference-error-msg').removeClass('visible').addClass('hidden');
                differenceDataTable.ajax.reload();
                dataTable.ajax.reload();
            },
            error: function() {
                $('#save-items-btn').removeClass('loading');
                $('#items-difference-error-msg .header').text(/*[[#{admin.items.save.error}]]*/ 'admin.items.save.error');
                $('#items-difference-error-msg').removeClass('hidden').addClass('visible');
            },
            complete: function() {
                $('#save-items-btn').removeClass('loading');
            }
        });
        event.preventDefault();
    });
    differenceDataTable.on('xhr', function() {
        if ($.isEmptyObject(differenceDataTable.ajax.json())) {
            $('#items-difference-segment').slideUp();
			$('#items-difference-none').removeClass('hidden')
		} else {
            $('#items-difference-segment').slideDown();
			$('#items-difference-none').addClass('hidden');
		}
		$('#items-difference-loading').addClass('hidden');
	});
    // Items from the db
    var dataTable = $('#items').DataTable({
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
            $.get(/*[[@{/api/items}]]*/ '/api/items', $.param(parameters, true), function(json) {
                    callback({ draw: data.draw, recordsTotal: json.page.totalElements,
                        recordsFiltered: json.page.totalElements,
                        data: json._embedded === undefined ? {} : json._embedded.items
                    });
                }
            );
        },
        columnDefs: [
            { orderable: false, targets: [0, 4, 5, 6] }
        ],
        columns: [
            { data: null,
                render: function(data, type, full, meta) {
                    return $('<div>', {
                        'class': 'item-description',
                        'data-html': data.description,
                        'data-variation': 'wide'
                    }).append($('<img>', {
                        'class': 'ui rounded image',
                        src: '/img/items/' + data.image.full
                    })).wrap('<span>').parent().html();
                }
            },
            { data: 'name' },
            { data: 'group',
                render: function(data, type, full, meta) {
					return data ? data : /*[[#{none}]]*/ 'none';
                }
            },
            { data: 'requiredChampion',
                render: function(data, type, full, meta) {
					return data ? data : /*[[#{none}]]*/ 'none';
                }
            },
            { data: 'maps',
                render: function(data, type, full, meta) {
					return getMaps(data);
                }
            },
            { data: '_links.self.href',
                render: function(data, type, full, meta) {
                    var button = $('<button>', {
                        'data-item-id': data.substring(data.lastIndexOf('/') + 1, data.length),
                        'class': 'ui secondary button',
                        text: /*[[#{admin.item.overwrite}]]*/ 'admin.item.overwrite'
                    });
                    return button.wrap('<span>').parent().html();
                }
            },
            { data: '_links.self.href',
                render: function(data, type, full, meta) {
                    var button = $('<button>', {
                        'data-item-id': data.substring(data.lastIndexOf('/') + 1, data.length),
                        'class': 'negative ui button',
                        text: /*[[#{admin.item.delete}]]*/ 'admin.item.delete'
                    });
                    return button.wrap('<span>').parent().html();
                }
            }
        ],
        lengthMenu: [ [10, 25, 50, 100, 1000], [10, 25, 50, 100, 'All'] ],
        language: {
            emptyTable: /*[[#{dataTables.emptyTable(#{admin.items})}]]*/ 'dataTables.emptyTable',
            info: /*[[#{dataTables.info}]]*/ 'dataTables.info',
            infoEmpty: /*[[#{dataTables.infoEmpty}]]*/ 'dataTables.infoEmpty',
            infoFiltered: /*[[#{dataTables.infoFiltered}]]*/ 'dataTables.infoFiltered',
            processing: /*[[#{dataTables.processing}]]*/ 'dataTables.processing',
            search: /*[[#{dataTables.search}]]*/ 'dataTables.search',
            zeroRecords: /*[[#{dataTables.zeroRecords(#{admin.items})}]]*/ 'dataTables.zeroRecords',
            paginate: {
                next: /*[[#{dataTables.paginate.next}]]*/ 'dataTables.paginate.next',
                previous: /*[[#{dataTables.paginate.previous}]]*/ 'dataTables.paginate.previous'
            }
        },
        drawCallback: function(settings) {
            $('.item-description').popup();
        }
    });
    $('.items-search-input').keyup(function() {
        var inputs = [];
        $('.items-search-input').each(function() {
            if ($(this).val()) {
                inputs.push($(this).data('column-name') + '|' + $(this).val());
            }
        });
        dataTable.search(inputs).draw();
    });
    $('#items tbody').on('click', '.secondary', function(event) {
        $.ajax({
            type: 'POST',
            url: (/*[[@{/riot/items/}]]*/ '/riot/items/') + $(this).data('item-id'),
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
                $('#items-error-msg').removeClass('visible').addClass('hidden');
                dataTable.ajax.reload();
            },
            error: function() {
                $(event.target).removeClass('loading');
                $('#items-error-msg .header').text(/*[[#{admin.item.overwrite.error}]]*/ 'admin.item.overwrite.error');
                $('#items-error-msg').removeClass('hidden').addClass('visible');
            },
            complete: function() {
                $(event.target).removeClass('loading');
            }
        });
    });
    $('#items tbody').on('click', '.negative', function(event) {
        $.ajax({
            type: 'DELETE',
            url: (/*[[@{/api/items/}]]*/ '/api/items/') + $(this).data('item-id'),
            beforeSend: function() {
                $(event.target).addClass('loading');
            },
            headers: {
                'X-CSRF-TOKEN': /*[[${_csrf.token}]]*/ '_csrf.token'
            },
            success: function(data) {
                $('#items-error-msg').removeClass('visible').addClass('hidden');
                differenceDataTable.ajax.reload();
                dataTable.ajax.reload();
            },
            error: function() {
                $(event.target).removeClass('loading');
                $('#items-error-msg .header').text(/*[[#{admin.item.delete.error}]]*/ 'admin.item.delete.error');
                $('#items-error-msg').removeClass('hidden').addClass('visible');
            },
            complete: function() {
                $(event.target).removeClass('loading');
            }
        });
    });
    $('.message').click(function() {
        $(this).transition('fade');
    });
    function getMaps(data) {
		var maps = [];
		$.each(data, function (index, value) {
			if (value) {
				switch (parseInt(index)) {
					case crystalScarId:
						maps.push(' ' + crystalScar);
						break;
					case twistedTreelineId:
						maps.push(' ' + twistedTreeline);
						break;
					case summonersRiftId:
						maps.push(' ' + summonersRift);
						break;
					case howlingAbyssId:
						maps.push(' ' + howlingAbyss);
						break;
				}
			}
		});
		maps.sort();
		return maps;
	}
});
