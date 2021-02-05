<template>
  <div id="eventregistration">
    <h2>Persons</h2>
    <table id="persons-table">
      <tr>
        <th>Name</th>
        <th>Events</th>
        <th>Payment ID</th>
        <th>Amount ($)</th>
      </tr>

      <tr v-for="(person, i) in persons" v-bind:key="`person-${i}`">
        <td>{{person.name}}</td>
        <td>


          <ul>
            <li v-for="(event, i) in person.eventsAttended" v-bind:key="`event-${i}`" style="list-style-type: disc;">
              <span class='registration-event-name'>{{event.name}}</span>
            </li>
          </ul>
        </td>
        <td>
          <ul>
           <li v-for="(event, i) in person.eventsAttended" v-bind:key="`event-${i}`">
              <span class='paypal-id-input'>{{event.paypal != null?event.paypal.id:""}}</span>
            </li>
          </ul>
        </td>
        <td>
          <ul>
           <li v-for="(event, i) in person.eventsAttended" v-bind:key="`event-${i}`">
              <span class='paypal-amount-input'>{{event.paypal != null?event.paypal.amount:""}}</span>
            </li>
          </ul>
        </td>

      </tr>
      <tr>
        <td>
          <input id="create-person-person-name" type="text" v-model="newPerson" placeholder="Person Name">
        </td>
        
        <td>
          <p>
            <select id='create-person-person-type' v-model="personType">
              <option value="Person">Person</option>
              <option value="Organizer">Organizer</option>
           </select>
          </p>
        </td>
    
        
        <td>
          <button id="create-person-button" v-bind:disabled="!newPerson" @click="createPerson(personType, newPerson)">Create Person</button>
        </td>
        <td></td>
        <td></td>
      </tr>
    </table>

    <span v-if="errorPerson" style="color:red">Error: {{errorPerson}}</span>

    <hr>
    <h2>Events</h2>
    <table id='events-table'>
      <tr>
        <th>Name</th>
        <th>Date</th>
        <th>Start</th>
        <th>End</th>
        <th>Artist</th>

      </tr>
      <tr v-for="(event, i) in events" v-bind:id="event.name" v-bind:key="`event-${i}`">
        <td v-bind:id="`${event.name.replace(/\s/g, '-')}-name`">{{event.name}}</td>
        <td v-bind:id="`${event.name.replace(/\s/g, '-')}-date`">{{event.date}}</td>
        <td v-bind:id="`${event.name.replace(/\s/g, '-')}-starttime`">{{event.startTime}}</td>
        <td v-bind:id="`${event.name.replace(/\s/g, '-')}-endtime`">{{event.endTime}}</td>
        <td v-bind:id="`${event.name.replace(/\s/g, '-')}-artist`">{{event.artist}}</td>
      </tr>
      <tr>
        <td>
          <input id="event-name-input" type="text" v-model="newEvent.name" placeholder="Event Name">
        </td>
        <td>
          <input id="event-date-input" type="date" v-model="newEvent.date" placeholder="YYYY-MM-DD">
        </td>
        <td>
          <input id="event-starttime-input" type="time" v-model="newEvent.startTime" placeholder="HH:mm">
        </td>
        <td>
          <input id="event-endtime-input" type="time" v-model="newEvent.endTime" placeholder="HH:mm">
        </td>
        <td>
          <input id="event-artist-input" type="text" v-model="concertArtist" placeholder="Artist">
        </td>
        <td>
          <button id="event-create-button" v-bind:disabled="!newEvent.name" v-on:click="createEvent(newEvent)">Create</button>
        </td>
      </tr>
    </table>
    <span id="event-error" v-if="errorEvent" style="color:red">Error: {{errorEvent}}</span>
    <hr>

    <h2>Registrations</h2>
    <label>Person:
      <select id='registration-person-select' v-model="selectedPersonR">
        <option disabled value="">Please select one</option>
        <option v-for="(person, i) in persons" v-bind:key="`person-${i}`">{{person.name}}</option>
      </select>
    </label>
    <label>Event:
      <select id='registration-event-select' v-model="selectedEventR">
        <option disabled value="">Please select one</option>
        <option v-for="(event, i) in events" v-bind:key="`event-${i}`">{{event.name}}</option>
      </select>
    </label>
    <button id='registration-button' v-bind:disabled="!selectedPersonR || !selectedEventR" @click="registerEvent(selectedPersonR, selectedEventR)">Register</button>
    <br/>
    <span v-if="errorRegistration" style="color:red">Error: {{errorRegistration}}</span>
    
    
    <hr>
      <h2>Assign Organizer</h2>
      <label>Organizer:
        <select id='assign-selected-organizer' v-model="selectedOrganizer">
          <option disabled value="">Please select one</option>
          <option v-for="(organizer, i) in organizers" v-bind:key="`organizer-${i}`">{{organizer.name}}</option>
        </select>
      </label>
      <label>Event:
        <select id='assign-selected-event-organizer' v-model="selectedEventA">
          <option disabled value="">Please select one</option>
          <option v-for="(event, i) in events" v-bind:key="`event-${i}`">{{event.name}}</option>
        </select>
      </label>
        <button id='assign-button-organizer' v-bind:disabled="!selectedOrganizer || !selectedEventA" @click="organizeEvent(selectedOrganizer, selectedEventA)">Assign</button>
    <br/>
    <span v-if="errorAssign" style="color:red">Error: {{errorAssign}}</span> <br/>


    <hr>
      <h2>Pay for Registration with Paypal</h2>
            <label>Person:
              <select id='paypal-person-select' v-model="selectedPersonPaypal">
                <option disabled value="">Please select one</option>
                <option v-for="(person, i) in persons" v-bind:key="`person-${i}`">{{person.name}}</option>
              </select>
            </label>
             <label>Event:
              <select id='paypal-event-select' v-model="selectedEventPaypal">
                <option disabled value="">Please select one</option>
                <option v-for="(event, i) in events" v-bind:key="`event-${i}`">{{event.name}}</option>
              </select>
            </label>
            <br>
            <label>Email:
              <input id="paypal-id-input" type="text" v-model="email" placeholder="Email">
            </label>
            <label>Amount:
              <input id="paypal-amount-input" type="int" v-model="amount" placeholder="$CAD">
            </label>
            <button id='paypal-button' v-bind:disabled="!selectedPersonPaypal || !selectedEventPaypal || !email || !amount" @click="payForRegistration(selectedPersonPaypal, selectedEventPaypal)">Make Payment</button>
            <br>
            <span id="paypal-error" v-if="errorPayment" style="color:red">Error: {{errorPayment}}</span>

    










  </div>
</template>

<script src="./registration.js"></script>

<style>
#eventregistration {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  background: #f2ece8;
  margin-top: 60px;
}
.registration-event-name {
  display: inline-block;
  width: 25%;
}
.registration-event-name {
  display: inline-block;
}
h1, h2 {
  font-weight: normal;
}
ul {
  list-style-type: none;
  text-align: left;
}
a {
  color: #42b983;
}
</style>
