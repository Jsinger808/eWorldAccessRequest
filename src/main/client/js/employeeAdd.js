$(function () {

    let searchParams = new URLSearchParams(window.location.search)
    let rowID;
    var employee;
    var employeeAccessGroupIDArray = [];
    var table;

    if (searchParams.has('id')) {
            rowID = searchParams.get('id')
            console.log(rowID)
            $.ajax({
                url: "http://localhost:8082/api/v1/access/employee/id/" + rowID,
                type: 'GET',
                dataType: 'json', // added data type
                success: function(res) {
                    employee = res;
                    $('#full-name').val(employee.fullName)
                    $('#email').val(employee.email)
                    if (employee.bes) {
                        $('#BES').prop('checked', true);
                    }
                    if (employee.offshore) {
                        $('#offshore').prop('checked', true);
                    }
                    $.each(employee.employeeAccessGroupDTOs, function(key, val) {
                        employeeAccessGroupIDArray.push(val.accessGroupDTO.id);
                    })
                    createDataTable();
                }
            });
    }
    else {
        createDataTable();
    }




    //Creates Datatable for Access Groups
    function createDataTable() {
        console.log(employeeAccessGroupIDArray);
        table = $('#access-group-table').DataTable({
            ajax: {
                url: "http://localhost:8082/api/v1/access/access_group/",
                dataSrc: ""
            },
            "columns": [
                {data: "id"},
                {data: "name"},
                {data: "type"}
            ],
            'columnDefs': [{
                'targets': 0,
                'checkboxes': {
                    'selectRow': true
                },
                'createdCell':  function (td, cellData, rowData, row, col){
                    if(employeeAccessGroupIDArray.includes(rowData.id)) {
                        this.api().cell(td).checkboxes.select();
                    }
                }
            }],
            'order': [[2, 'asc']]
        });
    }

    // var accessGroupRows;
    //
    // setTimeout(function () {
    //     accessGroupRows = table.rows().data();
    // }, 300);

    // if (employee != null) {
    //         $('#full-name').val(employee.fullName)
    // }


    $("#my-form").submit(function (event) {
        event.preventDefault();
        console.log(employee)
        if (employee == null) {
            postEmployee();
        }
        else {
            putEmployee();
        }
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
            "accessGroupIDs": accessGroupIDsInput,
            "employeeAccessGroupDTOs" : []
        }
    }

    function accessGroupIDsArrayCreation() {
        var rows_selected = table.column(0).checkboxes.selected();
        if (rows_selected.length == 0) {
            return null
        }
        const idArray = (rows_selected.join(",")).split(",").map(Number);
        console.log("idArray " + idArray)
        return idArray
    }

    function postEmployee() {// pass your data in method
        $.ajax({
            type: "POST",
            url: "http://localhost:8082/api/v1/access/employee",
            data: JSON.stringify(createEmployeeJson()),// now data come in this function
            contentType: "application/json",
            crossDomain: true,
            dataType: "json",
            success: function (data, status, jqXHR) {
                alert("success");
                window.location=document.referrer;
            },
            error: function (request, status, error) {
                alert("Error Code: " + request.status + " - " + request.responseText);
            }
        });
    }

    function putEmployee() {// pass your data in method
        console.log(employee)
        $.ajax({
            type: "PUT",
            url: "http://localhost:8082/api/v1/access/employee/" + rowID,
            data: JSON.stringify(createEmployeeJson()),// now data come in this function
            contentType: "application/json",
            crossDomain: true,
            dataType: "json",
            success: function (data, status, jqXHR) {
                alert("success");
                window.location=document.referrer;
            },
            error: function (request, status, error) {
                alert("Error Code: " + request.status + " - " + request.responseText);
            }
        });
    }

});
