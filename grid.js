var vertical = false;

for (var y = 0; y < 9; y++) {
	$('table').append('<tr class="row_'+y+'"></tr>');
	for (var x = 0; x < 9; x++)
		$('table').find('tr.row_'+y).append('<td><input name="cell_'+y+'_'+x+'" style="background: black;"></td>');
}

$('#vertical').change(function() {
		vertical = $(this).attr('checked');
	});

$('input').keyup(function(event) {
		if (event.keyCode == 8 || event.keyCode == 37 || event.keyCode == 38 ||
			event.keyCode == 39 || event.keyCode == 40) {
			var y = parseInt($(this).attr('name').split('_')[1]);
			var x = parseInt($(this).attr('name').split('_')[2]);
			if (event.keyCode == 8) {
				$(this).val('');
				$(this).css('background', 'black');
				if (vertical) {
					if (y != 0) y--;
					else { x--; y = 8;}
				} else {
					if (x != 0) x--;
					else { y--; x = 8;}
				}
			}
			if (event.keyCode == 37) {
				if (x > 0) x--;
			}
			if (event.keyCode == 38) {
				if (y > 0) y--;
			}
			if (event.keyCode == 39) {
				if (x < 8) x++;
			}
			if (event.keyCode == 40) {
				if (y < 8) y++;
			}
			$('input[name="cell_'+y+'_'+x+'"]').focus();
		}
	});

$('input').keypress(function(event) {
		$(this).css('background', 'white');
		$(this).val('');
		var y = parseInt($(this).attr('name').split('_')[1]);
		var x = parseInt($(this).attr('name').split('_')[2]);
		if (vertical) {
			if (y != 8) y++;
			else { x++; y = 0;}
		} else {
			if (x != 8) x++;
			else { y++; x = 0;}
		}
		$('input[name="cell_'+y+'_'+x+'"]').focus();
	});