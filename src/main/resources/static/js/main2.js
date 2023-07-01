$(function () {


    $('#getDataButton').click(function () {
        $('.loaderArea').show();
        const daysCount = document.getElementById('days').value;
        $.ajax({
            method: "GET",
            url: '/' + daysCount,
            success: function () {
                location.reload();
            },
            error: function (response) {
                if (response.status === 404) {
                    alert('Invalid value');
                }
            }
        });

        return false;
    });

    $(document).ready(function() {
        $('.loaderArea').hide();
    });

});