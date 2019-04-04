function checkBillingAddress(){
    if($("#theSameAsShippingAddress").is(":checked")){
        $(".billingAddress").prop("disabled", true);
    }else{
        $(".billingAddress").prop("disabled", false);
    }
}

function checkPasswordMatch() {
    var password = $("#textNewPassword").val();
    var confirmPassword = $("#textConfirmPassword").val();
    // document.write("hehe");

    if(password == "" && confirmPassword == ""){
        console.log('empty');
        $("#checkPasswordMatch").html("");
        $("updateUserInfoButton").prop('disabled', false);
    }else{
        if(password != confirmPassword){
            console.log('doees not match');
            $("#checkPasswordMatch").html("Passwords do not match!");
            $("#checkPasswordMatch").css('color', 'red');
            $("#updateUserInfoButton").prop('disabled', true);
        }else{
            console.log('match');
            $("#checkPasswordMatch").html("Passwords match");
            $("#checkPasswordMatch").css('color', 'green');
            $("#updateUserInfoButton").prop('disabled', false);
        }
    }
}

$(document).ready(
    function () {
        $(".cartItemQty").on("change", function () {
            var id=this.id;
            $('#update-item-'+id).css('display', 'inline-block');
        });
        $("#theSameAsShippingAddress").on('click', checkBillingAddress);
        $("#textConfirmPassword").keyup(checkPasswordMatch);
    });
