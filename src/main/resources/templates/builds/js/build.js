$(function() {
    $('#map').dropdown();
    $('#items .item, #summoner-spells .item, #trinket .item').popup();
    // If map is Proving Grounds
    if ($('#map option:selected').val() == 12) {
        $('#trinket .image').addClass('disabled');
    }
    $('#build-url-input').val(window.location.protocol + '//' + window.location.host + window.location.pathname);
});