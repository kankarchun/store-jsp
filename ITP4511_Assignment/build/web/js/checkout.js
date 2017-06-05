document.getElementsByTagName("button")[0].onclick=function(){
    if (delivery.checked){
        if (deliverydateime.value==""||deliveryaddress.value==""){
            alert("your delivery infomation are not fill completed.");
        }else{
            location = 'checkout?action=success&method=delivery&datetime='+deliverydateime.value+"&address="+deliveryaddress.value;
        }
    }else{
        location = 'checkout?action=success&method=selfpickup'
    }
};