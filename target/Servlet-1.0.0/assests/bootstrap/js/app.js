$('#btnGetAll').click(function (){
    $.ajax({
        url:"http://localhost:8080/pos/customer",
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
                            </tr>`;
                $('#tblcustomers').append(row);
            }
        },
        error : function (error) {
            console.log("error: ", error);
        }
    })
})