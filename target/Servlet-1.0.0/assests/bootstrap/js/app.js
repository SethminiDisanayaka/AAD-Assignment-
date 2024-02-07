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
