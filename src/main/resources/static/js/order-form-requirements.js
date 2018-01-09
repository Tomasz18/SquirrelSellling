$(document).ready(function() {
    shippingUpdate();
    invoiceUpdate();
    postponementUpdate();

    $("#shippingRadio").change(shippingUpdate());
    $("#selfPickupRadio").change(shippingUpdate());
    $("#invoiceRadio").change(invoiceUpdate());
    $("#receiptRadio").change(invoiceUpdate());
    $("#postponementCheckbox").change(postponementUpdate());

    function shippingUpdate() {
        return function () {
            var shipping = $("#shippingRadio").is(':checked');
            $("#shippingStreetInput").prop('required', shipping);
            $("#shippingBuildingInput").prop('required', shipping);
            $("#shippingCityInput").prop('required', shipping);
            $("#shippingPostalCodeInput").prop('required', shipping);
            console.log("shippingUpdate");
            console.log(shipping);
        }
    }
    function invoiceUpdate() {
        return function () {
            var invoice = $("#invoiceRadio").is(':checked');
            $("#invoiceStreetInput").prop('required', invoice);
            $("#invoiceBuildingInput").prop('required', invoice);
            $("#invoiceCityInput").prop('required', invoice);
            $("#invoicePostalCodeInput").prop('required', invoice);
            console.log("invoiceUpdate");
            console.log(invoice);
        }
    }
    function postponementUpdate() {
        return function () {
            var postponement = $("#postponementCheckbox").is(':checked');
            $("#postponementDateInput").prop('required', postponement);
            console.log("postponementUpdate");
            console.log(postponement);
        }
    }
});