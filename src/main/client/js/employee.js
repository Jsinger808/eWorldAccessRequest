
var editor;

$(document).ready(function() {

    // editor = new $.fn.dataTable.Editor( {
    //     ajax: {
    //         url: "http://localhost:8082/api/v1/access/employee/",
    //         dataSrc: ""
    //     },
    //     table: "#example",
    //     fields: [ {
    //         "label": "Full Name:",
    //         "name": "fullName"
    //     }, {
    //         "label": "Email:",
    //         "name": "email"
    //     }, {
    //         "label": "BES:",
    //         "name": "bes"
    //     }, {
    //         "label": "Offshore:",
    //         "name": "offshore"
    //     },
    //         {
    //         "label" : "Access Groups:",
    //         "name": "employeeAccessGroupDTOs[, ].accessGroupDTO.name"
    //         }
    //     ]
    // } );

    // New record
    $('a.editor-create').on('click', function (e) {
        e.preventDefault();

        editor.create( {
            title: 'Create new record',
            buttons: 'Add'
        } );
    } );

    // Edit record
    $('#example').on('click', 'td.editor-edit', function (e) {
        e.preventDefault();

        editor.edit( $(this).closest('tr'), {
            title: 'Edit record',
            buttons: 'Update'
        } );
    } );

    // Delete a record
    $('#example').on('click', 'td.editor-delete', function (e) {
        e.preventDefault();

        editor.remove( $(this).closest('tr'), {
            title: 'Delete record',
            message: 'Are you sure you wish to remove this record?',
            buttons: 'Delete'
        } );
    } );

        $('#example').DataTable({
            ajax: {
                // url: "http://localhost:8082/api/v1/access/employee/",
                // dataSrc: ""
            },
            dom: 'Bfrtip',
            buttons: [
                {
                    text: 'Add New Employee',
                    action: function ( e, dt, node, config ) {
                        alert( 'Button activated' );
                    }
                }
            ],
            "columns": [
                { data : "fullName"},
                { data : "email"},
                { data : "bes"},
                { data : "offshore"},
                { data : "employeeAccessGroupDTOs[, ].accessGroupDTO.name"},
                {
                    data: null,
                    className: "dt-center editor-edit",
                    defaultContent: '<i class="fa fa-pencil"/>',
                    orderable: false
                },
                {
                    data: null,
                    className: "dt-center editor-delete",
                    defaultContent: '<i class="fa fa-trash"/>',
                    orderable: false
                }
            ]
        })
} );

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