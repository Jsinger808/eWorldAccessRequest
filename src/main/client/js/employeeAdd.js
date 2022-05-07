$(function() {

    var table = $('#access-group-table').DataTable({
        ajax: {
            url: "http://localhost:8082/api/v1/access/access_group/",
            dataSrc: ""
        },
        "columnDefs": [
            { "width": "0%", "targets": 0 },
            {
                'targets': 0,
                'checkboxes': {
                    'selectRow': true
                }
            }
        ],
        "columns": [
            {
                render: function (data, type, row) {
                    return '<input type="checkbox" data-something="' + row.id + '"> </input>';
                }
            },
            { data : "name"},
            { data : "type"}
        ],
    });

    // var accessGroupRows;
    //
    // setTimeout(function () {
    //     accessGroupRows = table.rows().data();
    // }, 300);

    $("#my-form").submit(function(event) {
        event.preventDefault();
        addData();
    });

    function createEmployeeJson() {
        var fullNameInput = $("#full-name").val();
        var emailInput = $("#email").val();
        var offshoreInput = $("#offshore").is(":checked");
        var BESInput = $("#BES").is(":checked");
        var accessGroupIDsInput = accessGroupIDsArrayCreation();

        return {
            "fullName": fullNameInput,
            "email": emailInput,
            "bes": BESInput,
            "offshore": offshoreInput,
            "accessGroupIDs": accessGroupIDsInput
        }
    }

    function accessGroupIDsArrayCreation() {

        var idArray = []

        var inputArray = $("#access-group-table input");


        $.each(inputArray, function(key, value) {
                if (($(value).is(':checked'))) {
                    idArray.push($(value).data("something"))
                }
                // console.log($(value).data("something"))
                // console.log($(value))
            });

        console.log(idArray);
        return idArray

            // var CheckedRow = $("#access-group-table tbody tr").filter(function() {
            //
            //     if ($(this).find('td:eq(0) > input[type="checkbox"]').is(':checked')) {
            //         return $(this);
            //     }
            // });
            //
            // console.log(CheckedRow);
            //
            // $.each(CheckedRow, function(key, value) {
            //     console.log($(value).data("something"))
            // });

    };

    function addData(){// pass your data in method
        console.log(createEmployeeJson());
        $.ajax({
            type: "POST",
            url: "http://localhost:8082/api/v1/access/employee",
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

} );

//
// $(function AddEmployee(){
//     return $.ajax({
//         type: "POST",
//         url: `http://localhost:8082/api/v1/access/access_group/`,
//         contentType : 'application/json',
//         dataType : "json",
//         data: JSON.stringify(buildUpdatedApplicationVO(applicationVO, 'appFinAcctsTransferInd')),
//         success: function(response) {
//             openSavedSuccessAlert();
//         },
//         error: function() {
//             openSavedFailureAlert();
//         }
//     })
// )};