$(document).ready(function(){
    var modules = [
        {radioName: 'pickup', container1Id: 'selfPickup', container2Id: 'shipping'},
        {radioName: 'bill', container1Id: 'receipt', container2Id: 'invoice'}
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
   var module = {checkboxName: 'postponement', container1Id: 'postponementNo', container2Id: 'postponementYes'};

   $('input[type=checkbox][name=' + module.checkboxName + ']').first().prop('checked', true);
   $("#" + module.container1Id).hide();
   $('input[type=checkbox][name=' + module.checkboxName + ']').change(generate_handler(module));


    function generate_handler(module) {
        return function() {
            $('#' + module.container1Id).toggle();
            $('#' + module.container2Id).toggle();
        }
    }
});