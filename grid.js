var vertical = false;
var size = 12;

function Word(word, x, y, desc) {
	var word;
	var x;
	var y;
	var desc;

	this.word = word;
	this.x = x;
	this.y = y;
	this.desc = desc;
}

for (var y = 0; y < size; y++) {
	$('table').append('<tr class="row_'+y+'"></tr>');
	for (var x = 0; x < size; x++)
		$('table').find('tr.row_'+y).append('<td><input name="cell_'+y+'_'+x+'" style="background: black;"></td>');
}

$('#vertical').change(function() {
		vertical = $(this).attr('checked');
	});

$('input').keyup(function(event) {

		if (event.keyCode == 27) {
			vertical = !vertical;
			$('#vertical').attr('checked', vertical ? 'checked' : null);
		}

		if (event.keyCode == 8 || event.keyCode == 37 || event.keyCode == 38 ||
			event.keyCode == 39 || event.keyCode == 40) {
			var y = parseInt($(this).attr('name').split('_')[1]);
			var x = parseInt($(this).attr('name').split('_')[2]);
			if (event.keyCode == 8) {
				$(this).val('');
				$(this).css('background', 'black');
				if (vertical) {
					if (y != 0) y--;
					else { x--; y = size - 1;}
				} else {
					if (x != 0) x--;
					else { y--; x = size - 1;}
				}
			}
			if (event.keyCode == 37) {
				if (x > 0) x--;
			}
			if (event.keyCode == 38) {
				if (y > 0) y--;
			}
			if (event.keyCode == 39) {
				if (x < size - 1) x++;
			}
			if (event.keyCode == 40) {
				if (y < size - 1) y++;
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
			if (y != size - 1) y++;
			else { x++; y = 0;}
		} else {
			if (x != size - 1) x++;
			else { y++; x = 0;}
		}
		$('input[name="cell_'+y+'_'+x+'"]').focus();
	});

$('input[name="make"]').click(function() {
		var cells = $('table').find('input');
		var h = new Array();
		var v = new Array();
		var word_h = null;
		var word_v = null;
		for (var x = 0; x < size; x++) {
			for (var y = 0; y < size; y++) {
				j = x * size + y;
				i = x + y * size;

				if (cells[j].value != "") {
					if (word_h == null)
						word_h = new Word("", x, y, "");
					word_h.word += cells[j].value;
				}
				if (cells[j].value == "" || y == size-1) {
					if (word_h != null && word_h.word.length > 1) h.push(word_h);
					word_h = null;
				}

				if (cells[i].value != "") {
					if (word_v == null)
						word_v = new Word("", x, y, "");
					word_v.word += cells[j].value;
				}
				if (cells[i].value == "" || y == size-1) {
					if (word_v != null && word_v.word.length > 1) v.push(word_v);
					word_v = null;
				}
			}
		}
		writeXML(h, v);
		writeDescription(h, v);
	});

function writeDescription(h, v) {
	var html = "Horizontal<br />";
	for (var i in h) {
		html += h[i].word + ": <input type=\"text\" name=\"desc_h\" id=\"desc_h_" + i + "\" value=\"" + h[i].desc + "\" /><br />";
	}
	html += "Vertical<br />";
	for (var i in v) {
		html += v[i].word + ": <input type=\"text\" name=\"desc_v\" id=\"desc_v_" + i + "\" value=\"" + v[i].desc + "\" /><br />";
	}
	$('#description').html(html);
}

function writeXML(h, v) {
	var descriptions = $('#description input');
	if (descriptions) {
		for (var d in descriptions) {
			if (descriptions[d] && descriptions[d].id) {
				var i = descriptions[d].id.split('_')[2];
				if (descriptions[d].id.split('_')[1] == 'h')
					h[i].desc = descriptions[d].value;
				else
					v[i].desc = descriptions[d].value;
			}
		}
	}

	var xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
	xml += "<grid>\n";
	xml += "<name>Grille 8</name>\n";
	xml += "<date>01/04/2012</date>\n";
	xml += "<category/>\n";
	xml += "<url/>\n";
	xml += "<author>Gwendoline Dufour</author>\n";
	xml += "<level>1</level>\n";
	xml += "<width>9</width>\n";
	xml += "<height>9</height>\n";
	xml += "<horizontal>\n";
	for (var i in h) {
		xml += "<word x=\"" + h[i].x + "\" y=\"" + h[i].y + "\" description=\"" + h[i].desc + "\">" + h[i].word + "</word>\n";
	}
	xml += "</horizontal>\n";
	xml += "<vertical>\n";
	for (var i in v) {
		xml += "<word x=\"" + v[i].x + "\" y=\"" + v[i].y + "\" description=\"" + v[i].desc + "\">" + v[i].word + "</word>\n";
	}
	xml += "</vertical>\n";
	xml += "</grid>\n";
	$('#xml').text(xml);
}
