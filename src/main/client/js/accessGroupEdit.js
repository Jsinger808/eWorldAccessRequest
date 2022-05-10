$(function () {

    var table = $('#access-group-table').DataTable({
        ajax: {
            url: "http://localhost:8082/api/v1/access/access_group/",
            dataSrc: ""
        },
        "columns": [
            {data: "name"},
            {data: "type"}
        ],
        'order': [[1, 'asc']]
    });

    // var accessGroupRows;
    //
    // setTimeout(function () {
    //     accessGroupRows = table.rows().data();
    // }, 300);

    $("#my-form").submit(function (event) {
        event.preventDefault();
        addData();

    });

    function createEmployeeJson() {
        var nameInput = $("#access-group-name").val();
        var typeInput = $("[name='access-group-type']:checked").val()

        console.log(typeInput)

        return {
            "name": nameInput,
            "type": typeInput,
        }


    }

    function addData() {// pass your data in method
        $.ajax({
            type: "POST",
            url: "http://localhost:8082/api/v1/access/access_group",
            data: JSON.stringify(createEmployeeJson()),// now data come in this function
            contentType: "application/json",
            crossDomain: true,
            dataType: "json",
            success: function (data, status, jqXHR) {
                alert("success");// write success in " "
            },

            error: function (jqXHR, status) {
                // error handler
                console.log(jqXHR);
                alert('fail' + status.code);
            }
        });
    }

});
