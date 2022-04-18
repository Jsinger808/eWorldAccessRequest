$(document).ready(function() {


    $('#example').DataTable({
        ajax: {
            url: "http://localhost:8082/api/v1/access/access_group/",
            dataSrc: ""
        },
        "columnDefs": [
            { "width": "2%", "targets": 2 }
        ],
        "columns": [
            { data : "name"},
            { data : "type"},
            { render : function (data, type, row) {
                    return '<input type="checkbox";</input>';
                }}
        ],
    })

    $("#myForm").submit(function(event) {
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

    }

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