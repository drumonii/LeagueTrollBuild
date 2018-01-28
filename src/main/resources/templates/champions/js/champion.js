$(function() {
    $('#map').dropdown();
    $('.message').click(function() {
        $(this).transition('fade');
    });
    new Clipboard('#copy-button');
    var newBuildBtn = $('#new-build');
    newBuildBtn.popup({
        on: 'manual',
        inline: true
    });

    var trollBuild = function getTrollBuild() {
        $.ajax({
            url: /*[[@{/api/champions/__${champion.id}__/troll-build}]]*/ '/api/champions/1/troll-build',
            beforeSend: function() {
                newBuildBtn.addClass('loading');
                $('#build-segment').addClass('loading');
            },
            data: {
                mapId: $('#map option:selected').val()
            },
            success: function(data) {
                $('#champion-error-msg').hide();
                newBuildBtn.popup('hide');
                $.each(data, function(id, list) {
                    var html = [];
                    $.each(list, function(i, element) {
                        var img = element.image.full;
                        html.push(
                            $('<div>', {
                                class: 'item',
                                'data-html': '<h4>' + element.name + '</h4>' + element.description,
                                'data-variation': 'wide',
                                'data-key': id,
                                'data-id': element.id,
                                'data-order': i + 1
                            }).append($('<img>', {
                                class: 'ui rounded image',
                                src: '/img/' + $('#' + id).data('model') + '/' + element.id +
                                    img.substring(img.lastIndexOf('.'), img.length)
                            }))
                        );
                    });
                    $('#' + id).html(html);
                    $('#items .item, #summoner-spells .item, #trinket .item').popup();
                });
                $('#build-submit-btn').removeClass('disabled');
                $('#build-submit-btn').text(/*[[#{builds.save}]]*/ 'builds.save');
                $('#build-url-input').val('');
            },
            error: function() {
                $('#champion-error-msg .header').text(/*[[#{champion.trollBuild.error}]]*/ 'champion.trollBuild.error');
                $('#champion-error-msg').show();
            },
            complete: function() {
                newBuildBtn.removeClass('loading');
                $('#build-segment').removeClass('loading');
                // If map is Proving Grounds
                if ($('#map option:selected').val() == 12) {
                    $('#trinket .image').addClass('disabled');
                } else {
                    $('#trinket .image').removeClass('disabled');
                }
            }
        });
    };
    trollBuild();

    newBuildBtn.click(function() {
        trollBuild();
    });
    $('#map').change(function () {
        trollBuild();
    });

    $('#build-submit-btn').click(function(event) {
        var data = {
            champion: $('#champion-header').data('champion-id'),
            item1: $('.item').filter('[data-key="items"][data-order="1"]').data('id'),
            item2: $('.item').filter('[data-key="items"][data-order="2"]').data('id'),
            item3: $('.item').filter('[data-key="items"][data-order="3"]').data('id'),
            item4: $('.item').filter('[data-key="items"][data-order="4"]').data('id'),
            item5: $('.item').filter('[data-key="items"][data-order="5"]').data('id'),
            item6: $('.item').filter('[data-key="items"][data-order="6"]').data('id'),
            summonerSpell1: $('.item').filter('[data-key="summoner-spells"][data-order="1"]').data('id'),
            summonerSpell2: $('.item').filter('[data-key="summoner-spells"][data-order="2"]').data('id'),
            trinket: $('.item').filter('[data-key="trinket"][data-order="1"]').data('id'),
            map: $('#map option:selected').data('map-id')
        };
        $.ajax({
            type: 'POST',
            url: /*[[@{/api/builds}]]*/ '/api/builds',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data),
            headers: {
                'X-CSRF-TOKEN': /*[[${_csrf.token}]]*/ '_csrf.token'
            },
            success: function(data) {
                var url = data['_links'].self.href;
                $('#build-url-input').val(url.replace(new RegExp(/\/(api)/, 'g'), ''));
                $('#build-submit-btn').addClass('disabled');
                $('#build-submit-btn').text(/*[[#{builds.saved}]]*/ 'builds.saved');
            }
        });
        event.preventDefault();
    });
});
