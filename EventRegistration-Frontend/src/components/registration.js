import _ from 'lodash';
import axios from 'axios';
let config = require('../../config');

let backendConfigurer = function () {
  switch (process.env.NODE_ENV) {
    case 'testing':
    case 'development':
      return 'http://' + config.dev.backendHost + ':' + config.dev.backendPort;
    case 'production':
      return 'https://' + config.build.backendHost + ':' + config.build.backendPort;
  }
}

let backendUrl = backendConfigurer();

let AXIOS = axios.create({
  baseURL: backendUrl
  // headers: {'Access-Control-Allow-Origin': frontendUrl}
});

export default {
  name: 'eventregistration',
  data() {
    return {
      persons: [],
      organizers: [],
      events: [],
      newPerson: '',
      personType: 'Person',
      newEvent: {
        name: '',
        date: '2017-12-08',
        startTime: '09:00',
        endTime: '11:00',
        artist: ''
      },
      selectedPersonR: '',
      selectedEventR: '',
      selectedOrganizer: '',
      selectedEventA: '',
      selectedPersonPaypal: '',
      selectedEventPaypal: '',
      errorPerson: '',
      errorOrganizer: '',
      errorEvent: '',
      errorRegistration: '',
      errorPayment: '',
      errorAssign: '',
      email: '',
      amount: '',
      concertArtist: '',
      response: [],
    }
  },
  created: function () {
    // Initializing persons
    AXIOS.get('/persons')
    .then(response => {
      this.persons = response.data;
      this.persons.forEach(person => this.getRegistrations(person.name))
    })
    .catch(e => {this.errorPerson = e});

    AXIOS.get('/events').then(response => {this.events = response.data}).catch(e => {this.errorEvent = e});

    AXIOS.get('/organizers').then(response => {this.organizers = response.data}).catch(e => {this.errorOrganizer = e});

  },

  methods: {

    createPerson: function (personType, personName) {
      if(personType =="Person"){
      AXIOS.post('/persons/'.concat(personName), {}, {})
      .then(response => {
        this.persons.push(response.data);
        this.errorPerson = '';
        this.newPerson = '';
      })
      .catch(e => {
        e = e.response.data.message ? e.response.data.message : e;
        this.errorPerson = e;
        console.log(e);
      });

    }

    if(personType == "Organizer"){
	    	  AXIOS.post('/organizers/'.concat(personName), {}, {})
	          .then(response => {
	            this.persons.push(response.data);
	            this.organizers.push(response.data);
	            this.errorPerson = '';
	            this.newPerson = '';
	          })
	          .catch(e => {
	              e = e.response.data.message ? e.response.data.message : e;
	              this.newPerson = '';
	              this.errorPerson = e;
	              console.log(e);
	          });
	      }
    },

    createEvent: function (newEvent) {
      let url = '';

      if(this.concertArtist == null || this.concertArtist == ''){
	      AXIOS.post('/events/'.concat(newEvent.name), {}, {params: newEvent})
	      .then(response => {
	        this.events.push(response.data);
	        this.errorEvent = '';
	        this.newEvent.name = this.newEvent.make = this.newEvent.movie = this.newEvent.company = this.newEvent.artist = this.newEvent.title = '';
	      })
	      .catch(e => {
	        e = e.response.data.message ? e.response.data.message : e;
	        this.errorEvent = e;
	        console.log(e);
	      });
      } else {
    	  AXIOS.post('/concerts/' + newEvent.name + '/' + this.concertArtist, {}, {params: newEvent})
	      .then(response => {
	        this.events.push(response.data);
	        this.errorEvent = '';
	        this.concertArtist = '';
	        this.newEvent.name = this.newEvent.make = this.newEvent.movie = this.newEvent.company = this.newEvent.artist = this.newEvent.title = '';

	      })
	      .catch(e => {
	        e = e.response.data.message ? e.response.data.message : e;
	        this.errorEvent = e;
	        console.log(e);
	      });
      }
    },

    registerEvent: function (personName, eventName) {
      let event = this.events.find(x => x.name === eventName);
      let person = this.persons.find(x => x.name === personName);
      let params = {
        person: person.name,
        event: event.name
      };

      AXIOS.post('/register', {}, {params: params})
      .then(response => {
        person.eventsAttended = response.data.person.eventsAttended;
        this.selectedPersonR = '';
        this.selectedEventR = '';
        this.errorRegistration = '';
      })
      .catch(e => {
        e = e.response.data.message ? e.response.data.message : e;
        this.errorRegistration = e;
        console.log(e);
      });
    },

    getRegistrations: function (personName) {
      AXIOS.get('/events/person/'.concat(personName))
      .then(response => {
        if (!response.data || response.data.length <= 0) return;

        let indexPart = this.persons.map(x => x.name).indexOf(personName);
        this.persons[indexPart].eventsAttended = [];
        response.data.forEach(event => {
          this.persons[indexPart].eventsAttended.push(event);
        });
      })
      .catch(e => {
        e = e.response.data.message ? e.response.data.message : e;
        console.log(e);
      });
    },

    payForRegistration: function (personName, eventName){
    	let event = this.events.find(x => x.name === eventName);
        let person = this.persons.find(x => x.name === personName);
        let params = {
          person: person.name,
          event: event.name
        };

    	AXIOS.post('/payment?email=' + this.email + '&amount=' + this.amount, {}, {params: params})
    	.then(response => {
            let person = this.persons.find(x => x.name === personName);
            person.eventsAttended[person.eventsAttended.length - 1].paypal = response.data.eventsAttended[response.data.eventsAttended.length - 1].paypal;

    		this.email = '';
            this.amount = '';
            this.selectedPersonPaypal = '';
            this.selectedEventPaypal = '';
            this.errorPayment = '';
          })
          .catch(e => {
        	  this.email = '';
              this.amount = '';
        	this.errorPayment = e.response.data.message ? e.response.data.message : e;
            console.log(e);
          });
    },

    organizeEvent: function (personName, eventName){
    	let event = this.events.find(x => x.name === eventName);
        let person = this.persons.find(x => x.name === personName);
        let params = {
          person: person.name,
          event: event.name
        };

        AXIOS.post('/assignOrganizer', {}, {params: params})
        .then(response => {
            this.selectedOrganizer = '';
            this.selectedEventA = '';
          })
          .catch(e => {
            e = e.response.data.message ? e.response.data.message : e;
            this.errorAssign = e;
            console.log(e);
          });
    },

    getPayment: function (personName, eventName){
    	let event = this.events.find(x => x.name === eventName);
        let person = this.persons.find(x => x.name === personName);
        let params = {
          person: person.name,
          event: event.name
        };

    	AXIOS.get('/registrations/paypal', {}, {params: params})
    	.then(response => {
    		if (!response.data || response.data.length <= 0) return;

            let indexPart = this.persons.map(x => x.name).indexOf(personName);
            this.persons[indexPart].eventsPaid = [];
            response.data.forEach(paypal => {
                this.persons[indexPart].eventsPaid.push(paypal);
            });
    	})
          .catch(e => {
            e = e.response.data.message ? e.response.data.message : e;
            this.errorPayment = e;
            console.log(e);
        });
    }




  }
}




























