

$(document).ready(function() {


        $('#example').DataTable({
            dom: 'Bfrtip',
            buttons: [
                'copy', 'csv', 'excel', 'pdf', 'print',                 {
                    text: 'Add New Employee',
                    action: function ( e, dt, node, config ) {
                        document.location.href ="../html/employeeAdd.html";
                    }
                }
            ],
            ajax: {
                url: "http://localhost:8082/api/v1/access/employee/",
                dataSrc: ""
            },
            "columns": [
                { data : "fullName"},
                { data : "email"},
                { data : "bes"},
                { data : "offshore"},
                { data : "employeeAccessGroupDTOs[, ].accessGroupDTO.name"},
                { render : function (data, type, row) {
                    return '<button class="employee-addbtn" onclick="document.location.href=\'employeeAdd.html\';" >Update</button>';
                    }}
            ],
        })

} );

$(".employee-addbtn").on("click",function(){
    document.location.href ="/html/employeeAdd.html";
});


// {
//     data: null,
//     className: "dt-center editor-edit",
//     defaultContent: '<i class="fa fa-pencil"/>',
//     orderable: false
// },
// {
//     data: null,
//     className: "dt-center editor-delete",
//     defaultContent: '<i class="fa fa-trash"/>',
//     orderable: false
// }
// var table = $('#myTable').DataTable();
//
// table.rows( { selected: true } ).data();
// table.cells( { selected: true } ).data();
//
// $('#example').on( 'click', 'tbody tr', function () {
//     if ( table.row( this, { selected: true } ).any() ) {
//         table.row( this ).deselect();
//     }
//     else {
//         table.row( this ).select();
//     }
// } );
//
//
//
// $(function() {
//     alert("HELLO EMPLOYEE PAGE");
//
//     async function getEmployee() {
//         const response = await fetch('http://localhost:8082/api/v1/access/employee/');
//         const data = await response.json(); //extract JSON from the http response
//
//         console.log(data);
//
//         $('#myTable').DataTable( {
//             ajax : '../data/data.json',
//             columns : [
//                     { data : 'name' },
//                     { data : 'gender' },
//                     { data : 'designation' }
//             ]
//
//             // "columns" : [
//             //         { data : 'fullName' },
//             //         { data: 'email' },
//             //         { data: 'bes' },
//             //         { data: 'offshore' }
//             // ]
//             // ajax: {
//             //     url: '/api/v1/access/employee',
//             //     dataSrc: 'employee'
//             // },
//             // columns: [
//             //     { employee: 'fullName' },
//             //     { employee: 'email' },
//             //     { employee: 'bes' },
//             //     { employee: 'offshore' }
//             //     ]
//         })
//     }
//
//     getEmployee();
// });