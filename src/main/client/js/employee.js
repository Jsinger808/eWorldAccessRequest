$(document).ready(function () {

    $('#example').DataTable({
        dom: 'Bfrtip',
        buttons: [
            'copy', 'csv', 'excel', 'pdf', 'print', {
                text: 'Add New Employee',
                action: function (e, dt, node, config) {
                    document.location.href = "../html/employeeAdd.html";
                }
            }
        ],
        ajax: {
            url: "http://localhost:8082/api/v1/access/employee/",
            dataSrc: ""
        },
        "columns": [
            {data: "fullName"},
            {data: "email"},
            {data: "bes"},
            {data: "offshore"},
            {data: "employeeAccessGroupDTOs[, ].accessGroupDTO.name"},
            /* EDIT */ {
                mRender: function (data, type, row) {
                    return '<button class="changeButton" id="' + row.id + '" onclick="editClick(this)">Edit</button>'
                }
            },
            /* DELETE */ {
                mRender: function (data, type, row) {
                    return '<button class="deleteButton" id="' + row.id + '" onclick="deleteClick(this)">Delete</button>'
                }
            },
        ],
    })
});


