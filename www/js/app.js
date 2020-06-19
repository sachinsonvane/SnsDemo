// Dom7
var $$ = Dom7;

// Theme
var theme = 'auto';
if (document.location.search.indexOf('theme=') >= 0) {
  theme = document.location.search.split('theme=')[1].split('&')[0];
}

// Init App
var app = new Framework7({
  id: 'com.sns',
  root: '#app',
  theme: theme,
  routes: routes, 
});

$$(document).on('click','#start-speech-to-txt',function(ev){

    SNS.callSpeakToText('start speech', function (successRes) {
        //alert('success '+successRes);
        $$('#speech-to-txt-result-value').html(successRes);
    }, function (reason) {
        alert(reason);
    });
});


$$(document).on('click','#start-txt-to-speech',function(ev){

  app.dialog.prompt('Enter your speech text.',"Speech Text" ,function (name) {
    
    if(name==""||name.length==0){
      alert("Please enter your speech text.");
    }else{
      SNS.callSpeak(name, function () {
        //alert('success');
      }, function (reason) {
          alert(reason);
      });
    }

  });

    
});
