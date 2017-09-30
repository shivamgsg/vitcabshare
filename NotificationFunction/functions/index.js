'use strict'

const functions = require('firebase-functions');
const admin =require('firebase-admin');
admin.initializeApp(functions.config().firebase);


exports.sendNotification=functions.database.ref('/Notification/{user_id}/{Notification_id}').onWrite(event =>{

const user_id=event.params.user_id;
const Notification_id=event.params.Notification_id;

console.log('We have a notification to send to :',user_id);

if(!event.data.val()){

  return console.log('A notification  has been deleted from the database :',Notification_id);


}

const fromuser=admin.database().ref(`/Notification/${user_id}/${Notification_id}`).once('value');
return fromuser.then(fromuserResult =>{

  const from_user_id=fromuserResult.val().from;
  console.log('You have a new notification:',from_user_id);

  const userQuery =admin.database().ref(`travel/${from_user_id}/name`).once('value');
  const deviceToken=admin.database().ref(`/travel/${user_id}/token`).once('value');

  return Promise.all([userQuery,deviceToken]).then(result =>{

    const username=result[0].val();
    const token_id=result[1].val();

      const payload={
        notification:{
          title:"Request",
          body:`${username} has sent you a new Request`,
          icon:"default",
          click_action:"in.app.mridul.vitcabshare_TARGET_NOTIFICATION"
        },
        data:{
          from_user_id:from_user_id
        }
      };
      return admin.messaging().sendToDevice(token_id,payload).then(response =>{

        console.log('This was the notification feature');
      });



    });


  });

});
