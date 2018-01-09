$(document).ready(function() {
    var items = $(".card");

    items.each(function(index) {
        var input = $(this).find('input')
        var costDiv = input.parent().next().children().first();

        var retailPrice = parseFloat(input.next().text());
        var wholesalePrice = parseFloat(input.next().next().text());
        var wholesaleThreshold = parseFloat(input.next().next().next().text());
        ware = {retailPrice: retailPrice, wholesalePrice: wholesalePrice, wholesaleThreshold: wholesaleThreshold};

        input.on('mouseup keyup', generate_handler(costDiv, ware));

    });

    function parseFormattedNumberToFloat(text, from, fromEnd) {
        return parseFloat($.trim(text.substring(from, text.length - fromEnd)).replace(/\s+/g, '').replace(',', '.'))
    }

    function formatToPLN(text) {
        return text.toLocaleString('pl-PL', {style: 'currency', currency: 'PLN'});
    }

    function generate_handler(costDiv, ware) {
            return function() {
                var quantity = parseFloat($(this).val());
                quantity = isNaN(quantity) ? 0 : quantity;
                var cost = quantity < ware.wholesaleThreshold ? ware.retailPrice  * quantity : ware.wholesalePrice * quantity;

                var text = costDiv.text();
                var previousCost = parseFormattedNumberToFloat(text, 0, 3);

                text = $('#totalCost').text();
                var totalCost = parseFormattedNumberToFloat(text, 5, 3);

                var newTotalCost = totalCost - previousCost + cost;

                costDiv.text(formatToPLN(isNaN(cost) ? 0 : cost));
                $('#totalCost').text('Suma: ' + formatToPLN(newTotalCost));
                $('#cartTotalCost').text(formatToPLN(newTotalCost));
            }
        }
});