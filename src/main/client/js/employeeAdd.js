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