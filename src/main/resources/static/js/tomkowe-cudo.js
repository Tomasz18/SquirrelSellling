$(document).ready(function(){
    var modules = [
        {radioName: 'personalCollection', container1Id: 'selfPickup', container2Id: 'shippingContainer'},
        {radioName: 'invoice', container1Id: 'receipt', container2Id: 'invoiceContainer'}
    ];
 
    for (var i = 0; i < modules.length; i++) {
        $('input[type=radio][name=' + modules[i].radioName + ']').first().prop('checked', true);
        $('#' + modules[i].container2Id).hide();
        $('input[type=radio][name=' + modules[i].radioName + ']').change(generate_handler(modules[i]));
    }
 
    function generate_handler(module) {
            return function() {
                $('#' + module.container1Id).toggle();
                $('#' + module.container2Id).toggle();
            }
        }
});

$(document).ready(function() {
   var module = {checkboxId: 'postponementCheckbox', container1Id: 'postponementYes', container2Id: 'postponementNo'};

   $('input[type=checkbox][id=' + module.checkboxId + ']').first().prop('checked', false);
   $("#" + module.container1Id).hide();
   $('input[type=checkbox][id=' + module.checkboxId + ']').change(generate_handler(module));


    function generate_handler(module) {
        return function() {
            $('#' + module.container1Id).toggle();
            $('#' + module.container2Id).toggle();
        }
    }
});