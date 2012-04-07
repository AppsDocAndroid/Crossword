/* attach a submit handler to the form */
$("#searchForm").submit(function(event) {

		/* stop form from submitting normally */
		event.preventDefault(); 
        
		/* get some values from elements on the page: */
		var $form = $( this ),
			term = $form.find( 'textarea[name="message"]' ).val(),
			url = $form.attr( 'action' );
		
		/* Hide buttons and show loading */
		$("input[type='submit']").attr("disabled", "disabled");
		$("input[type='submit']").val("Sending...");
		
		/* Send the data using post and put the results in a div */
		$.post( url, { message: term },
				function( data ) {
					var content = $( data ).find( '#content' );
					$( "#result" ).empty().append( content );
				}
				)
			.success(function() { console.log("success"); })
			.error(function(e) { $("#result")[0].innerHTML = e.responseText; })
			.complete(function() {
					$("input[type='submit']").attr("disabled", null);
					$("input[type='submit']").val("Send");
				});
	});
