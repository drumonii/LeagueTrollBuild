$(function() {
    var dataTable = $('#job-instances').DataTable({
        processing: true,
        serverSide: true,
        order: [ [1, 'desc'] ],
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
            $.get(/*[[@{/api/job-instances}]]*/ '/api/job-instances', $.param(parameters, true), function(json) {
                    callback({ draw: data.draw, recordsTotal: json.totalElements,
                        recordsFiltered: json.totalElements,
                        data: json.content
                    });
                }
            );
        },
        columnDefs: [
            { orderable: false, targets: [4] },
            { targets: [3], createdCell: function (td, cellData, rowData, row, col) {
                    switch(cellData) {
                        case 'COMPLETED':
                            $(td).addClass('positive');
                            break;
                        case 'FAILED':
                            $(td).addClass('negative');
                            break;
                    }
                }
            }
        ],
        columns: [
            { data: 'name' },
            { data: 'jobExecution.startTime' },
            { data: 'jobExecution.endTime' },
            { data: 'jobExecution.status' },
            { data: 'id',
                render: function(data, type, full, meta) {
                    var ctx = /*[[@{/admin/job-instances/}]]*/ '/admin/job-instances/';
                    var a = $('<a/>', {
                        'class': 'ui button',
                        href: ctx + data + '/step-executions',
                        text: /*[[#{admin.stepExecutions}]]*/ 'admin.stepExecutions'
                    });
                    return a.wrap('<span>').parent().html()
                }
            }
        ],
        lengthMenu: [ [10, 25, 50, 100, 1000], [10, 25, 50, 100, 'All'] ],
        language: {
            emptyTable: /*[[#{dataTables.emptyTable(#{admin.jobInstances})}]]*/ 'dataTables.emptyTable',
            info: /*[[#{dataTables.info}]]*/ 'dataTables.info',
            infoEmpty: /*[[#{dataTables.infoEmpty}]]*/ 'dataTables.infoEmpty',
            infoFiltered: /*[[#{dataTables.infoFiltered}]]*/ 'dataTables.infoFiltered',
            processing: /*[[#{dataTables.processing}]]*/ 'dataTables.processing',
            search: /*[[#{dataTables.search}]]*/ 'dataTables.search',
            zeroRecords: /*[[#{dataTables.zeroRecords(#{admin.jobInstances})}]]*/ 'dataTables.zeroRecords',
            paginate: {
                next: /*[[#{dataTables.paginate.next}]]*/ 'dataTables.paginate.next',
                previous: /*[[#{dataTables.paginate.previous}]]*/ 'dataTables.paginate.previous'
            }
        }
    });
    $('.job-instances-search-input').keyup(function() {
		var inputs = [];
		$('.job-instances-search-input').each(function() {
			if ($(this).val()) {
				inputs.push(JSON.stringify({
					column: $(this).data('column-name'),
					values: $(this).val()
				}));
			}
		});
		dataTable.search(inputs).draw();
    });
});
