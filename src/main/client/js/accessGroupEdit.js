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
                alert("success");
                $('#access-group-table').DataTable().ajax.reload();
            },

            error: function (request, status, error) {
                alert("Error Code: " + request.status + " - " + request.responseText);
            }
        });
    }

});
