# Copyright 2015 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
from bookshelf import get_model
from flask import Blueprint, redirect, render_template, request, url_for, jsonify
from google.cloud import storage
from google.cloud.storage import blob
from werkzeug.utils import secure_filename
import io
import os


crud = Blueprint('crud', __name__)

# [START list]
@crud.route("/user/androidreservationsdisplay/<user_email>")
def display_reservations_android():
    reservations= get_model().list_reservations(user_email)
    if request.headers.get("User-Agent") == "Android":
        return jsonify(reservations)
    return render_template("allevents.html", reservations=reservations)



@crud.route('/androidlistevents')
def list_a():
    users = get_model().list_ev()
    if request.user_agent.platform == "android":
        return jsonify(users)
    return render_template('welcome.html')




@crud.route('/loginandroid')
def list_d():
    users = get_model().list_users()
    if request.user_agent.platform == "android":
        return jsonify(users)
    return render_template('welcome.html')



@crud.route("/")
def list():
    token = request.args.get('page_token', None)
    if token:
        token = token.encode('utf-8')

    books, next_page_token = get_model().list(cursor=token)

    return render_template(
        "welcome.html",
        books=books,
        next_page_token=next_page_token)
# [END list]


# [START list]
@crud.route("/user/list")
def list_events():
    token = request.args.get('page_token', None)
    if token:
        token = token.encode('utf-8')
    #books, next_page_token = get_model().list(cursor=token)
    events_two, next_page_token = get_model().list(cursor=token)
    if request.user_agent.platform == "android":
            return jsonify(events_two)
    return render_template("list.html", events_two =events_two)
# [END list]


# [START list]
@crud.route("/user/reservationsdisplay/<user_email>")
def display_reservations(user_email):
    if request.headers.get("User-Agent") == "Android":
        users = get_model().list_reservations(user_email)
        return jsonify(users)
    
    reservations= get_model().list_reservations(user_email)

     
    return render_template("allevents.html", reservations =reservations)
# [END list]



# [START list]
@crud.route("/user/reservations")
def list_reservations():

    if request.method == 'POST':
        user_email = request.form.to_dict(flat=True)
        
        return redirect(url_for('/user/reservationsdisplay', user_email = user_email))
    return render_template("enteremail.html")
# [END list]



@crud.route('/user/venuedisplay/<venueid>')
def venuedisplay(venueid):
    events_two= get_model().list_events(venueid)
    return render_template("display.html", events_two = events_two)

@crud.route('/user/venuesearch/')
def venuesearch():
    venues = get_model().list_venues()
    if request.method == 'POST':
        venueid = request.form.to_dict(flat=True)
        return redirect(url_for('/venuedisplay', venueid = venueid))
    return render_template("venuesearch.html", venues = venues)



@crud.route('/user/datedisplay/<date>')
def datedisplay(date):
    events_two= get_model().list_e(date)
    return render_template("displaydate.html", events_two = events_two)

@crud.route('/user/datesearch/')
def datesearch():
    if request.method == 'POST':
        date = request.form.to_dict(flat=True)
        return redirect(url_for('/datedisplay', date = date))
    return render_template("datesearch.html")




@crud.route('/user/options')
def select():
    return render_template("options.html")

@crud.route('/user/login')
def select_one():
    return render_template("login.html")

@crud.route('/admin/login')
def select_two():
    return render_template("login_admin.html")


@crud.route('/admin/adminoptions')
def admin_options():
    return render_template("adminoptions.html")



@crud.route('/user/<id>')
def view(id):
    book = get_model().read(id)
    return render_template("view.html", book=book)


# [START add]
@crud.route('/user/add', methods=['GET', 'POST'])
def add():
    if request.method == 'POST':
        if request.headers.get("User-Agent") == "Android":
            bodyrequest = request.json
            newuserdict = {'name': bodyrequest['name'], 'email': bodyrequest['email'], 'instrument': bodyrequest['instrument']}
            user = get_model().create(newuserdict)
            return jsonify(user)
        else:
            data = request.form.to_dict(flat=True)
            user = get_model().create(data)
            return render_template("register.html", action="Add", user={})
    return render_template("register.html", action="Add", user={})

# [START add]
@crud.route('/user/join', methods=['GET', 'POST'])
def edit():
    if request.method == 'POST':
        if request.headers.get("User-Agent") == "Android":
            bodyrequest = request.json
            newresdict = {'user_email': bodyrequest['user_email'], 'event_id': bodyrequest['event_id']}
            reservation = get_model().create_reservation(newresdict)
            return jsonify(reservation)
        else: 
            data = request.form.to_dict(flat=True)
            reservation = get_model().create_reservation(data)
    return render_template("join.html", action="Add", reservation={})
# [END add]



# [START add]
@crud.route('/admin/addvenue', methods=['GET', 'POST'])
def add_venue():
    if request.method == 'POST':
        data = request.form.to_dict(flat=True)

        venues = get_model().create_venue(data)

        #return redirect(url_for('.view', id=book['id']))

    return render_template("addvenue.html", action="Add", venues={})
# [END add]


@crud.route('/admin/delete' , methods=['GET', 'POST'])
def delete():
    events_two, next_page_token = get_model().list()
    if request.method == 'POST':
        data = request.form['id']
        
        return redirect(url_for('deletevent', data = id))
    return render_template("deleteevent.html", events_two = events_two)

@crud.route('/admin/deleted/<id>' , methods=['GET', 'POST'])
def delete_two(id):
    get_model().delete_reservations(id)   
    get_model().delete(id)

    return render_template("adminoptions.html")



@crud.route('/admin/deleteuser' , methods=['GET', 'POST'])
def delete_user():
    if request.method == 'POST':
        data = request.form['id']
        
        return redirect(url_for('deletuser', data = id))
    return render_template("deleteuser.html")

@crud.route('/admin/deleteduser/<id>' , methods=['GET', 'POST'])
def deleted_user(id):
     
    get_model().delete_user(id)

    return render_template("adminoptions.html")


@crud.route('/admin/deletevenue' , methods=['GET', 'POST'])
def delete_venue():
    venues = get_model().list_venues()
    if request.method == 'POST':
        data = request.form['id']
        
        return redirect(url_for('deletvenue', data = id))
    return render_template("deletevenue.html", venues = venues)

@crud.route('/admin/deletedvenue/<id>' , methods=['GET', 'POST'])
def deleted_venue(id):
     
    #get_model().delete_reserve(id)
    #get_model().delete_event(id)
    get_model().delete_venue(id)

    return render_template("adminoptions.html")


import os
from flask import Flask,jsonify,session,redirect,url_for,escape,render_template,request,send_file
from google.cloud import storage
from google.cloud.storage import Blob
from werkzeug.utils import secure_filename
import io


@crud.route('/upload')
def upload_file():
   return render_template('upload.html')
        
@crud.route('/uploader', methods = ['GET', 'POST'])
def uploading():
   if request.method == 'POST':
      file = request.files['file']
      bucket_name = 'littletub'
      client = storage.Client()
      bucket = client.get_bucket(bucket_name)
      blob = bucket.blob(secure_filename(file.filename))
      try:
        blob.upload_from_file(file.stream)
        return jsonify({'success': True})
      except Exception as e:
        logging.exception(e)
        return jsonify({'success': False})


@crud.route('/look/<filename>')
def flook(filename):  
  try:
    # Reformat the filename using the bucket name fetched above
    bucket_name = 'info_bucket'
    client = storage.Client()
    bucket = client.get_bucket(bucket_name)
    blob = bucket.blob(secure_filename(filename))
    data = blob.download_as_string()
    extension = secure_filename(filename).rsplit('.', 1)[1]
    if extension == 'pdf':
        return send_file(io.BytesIO(data), attachment_filename=filename, mimetype='application.pdf')
    else:
        return data
  except Exception as e: 
    return jsonify({'error': 'Could not find file {}'.format(filename)})


@crud.route('/info/infooptions')
def infooptions():
    return render_template("infooptions.html")
    

