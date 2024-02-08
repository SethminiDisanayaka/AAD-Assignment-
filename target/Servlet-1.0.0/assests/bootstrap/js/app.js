$('#btnGetAll').click(function (){
    $.ajax({
        url:"http://localhost:8080/pos/customers",
        method:"GET",
        success:function (resp){
            console.log("success", resp);
            for (const customer of resp){
                console.log(customer.id);
                console.log(customer.name);
                console.log(customer.address);
                console.log(customer.contact);

                const row = `<tr>
                                <td>${customer.id}</td>
                                <td>${customer.name}</td>
                                <td>${customer.address}</td>
                                <td>${customer.contact}</td>
                            </tr>`;
                $('#tblcustomers').append(row);
            }
        },
        error : function (error) {
            console.log("error: ", error);
        }
    })
});

$('#btnSave').click(function () {
    const id = $('#txt-id').val();
    const name = $('#txt-name').val();
    const address = $('#txt-address').val();
    const contact = $('#txt-contact').val();

    const customerObj = {
        id: id,
        name: name,
        address: address,
        contact: contact
    };

    $.ajax({
        url: "http://localhost:8080/pos/customers",
        method: "POST",
        data: JSON.stringify(customerObj),
        contentType: "application/json",
        success: function (resp, textStatus, jqxhr) {
            console.log("success: ", resp);
            console.log("success: ", textStatus);
            console.log("success: ", jqxhr);

            if (jqxhr.status == 201) {
                alert(jqxhr.responseText);
            }
        },
        error: function (jqxhr, textStatus, error) {
            console.log("error: ", jqxhr);
            console.log("error: ", textStatus);
            console.log("error: ", error);
        }
    });
});

$('#btnDelete').click(function () {
    const id = $('#txt-id').val();

    $.ajax({
        url: "http://localhost:8080/app/customers?id=" + id,
        method: "DELETE",
        success: function (resp, textStatus, jqxhr) {
            console.log("success: ", resp);
            console.log("success: ", textStatus);
            console.log("success: ", jqxhr);
        },
        error: function (jqxhr, textStatus, error) {
            console.log("error: ", jqxhr);
            console.log("error: ", textStatus);
            console.log("error: ", error);
        }
    })
});

$('#btnUpdate').click(function () {
    const id = $('#txt-id').val();
    const name = $('#txt-name').val();
    const address = $('#txt-address').val();
    const contact =$('#txt-contact').val();

    const customerObj = {
        id:id,
        name:name,
        address:address,
        contact:contact
    };

    const jsonObj = JSON.stringify(customerObj);

    $.ajax({
        url: "http://localhost:8080/app/customers",
        method: "PUT",
        data: jsonObj,
        contentType: "application/json",
        success: function (resp, textStatus, jqxhr) {
            console.log("success: ", resp);
            console.log("success: ", textStatus);
            console.log("success: ", jqxhr);
        },
        error: function (jqxhr, textStatus, error) {
            console.log("error: ", jqxhr);
            console.log("error: ", textStatus);
            console.log("error: ", error);
        }
    })
});


$('#btnItemGetAll').click(function (){
    $.ajax({
        url:"http://localhost:8080/pos/items",
        method:"GET",
        success:function (resp){
            console.log("success", resp);
            for (const item of resp){
                console.log(item.itemId);
                console.log(item.description);
                console.log(item.unitPrice);
                console.log(item.quantity);

                const row = `<tr>
                                <td>${item.itemId}</td>
                                <td>${item.description}</td>
                                <td>${item.unitPrice}</td>
                                <td>${item.quantity}</td>
                            </tr>`;
                $('#tblItem').append(row);
            }
        },
        error : function (error) {
            console.log("error: ", error);
        }
    })
});

$('#onActionSaveItem').click(function () {
    const id = $('#itemID').val();
    const name = $('#description').val();
    const address = $('#unitPrice').val();
    const contact = $('#quantity').val();

    const itemObj = {
        itemId: itemId,
        description: description,
        unitPrice: unitPrice,
        quantity: quantity
    };

    $.ajax({
        url: "http://localhost:8080/pos/items",
        method: "POST",
        data: JSON.stringify(itemObj),
        contentType: "application/json",
        success: function (resp, textStatus, jqxhr) {
            console.log("success: ", resp);
            console.log("success: ", textStatus);
            console.log("success: ", jqxhr);

            if (jqxhr.status == 201) {
                alert(jqxhr.responseText);
            }
        },
        error: function (jqxhr, textStatus, error) {
            console.log("error: ", jqxhr);
            console.log("error: ", textStatus);
            console.log("error: ", error);
        }
    });
});