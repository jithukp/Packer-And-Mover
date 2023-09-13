from flask import *
from src.dbcon import *

app=Flask(__name__)

@app.route('/login',methods=['post'])
def login():
    username = request.form['username']
    password = request.form['password']
    qry = "select*from `user_registration` where username=%s and `password`=%s"
    val = (username, password)
    s = selectone(qry, val)

    if s is None:
        return jsonify({'task': 'invalid'})
    else:
        category=s[15]
        id = s[0]
        return jsonify({'task': 'valid', "id": id,"catogery":category})

@app.route('/register', methods=['post'])
def reg():
     try:
         fname = request.form['Fname']
         lname = request.form['Lname']
         phone = request.form['Phone']
         dob = request.form['Dob']
         licence_no = request.form['Licence']
         house_name = request.form['Housename']
         place = request.form['Place']
         post = request.form['Post']
         city = request.form['City']
         district = request.form['District']
         state = request.form['State']
         pin = request.form['Pin']
         username = request.form['Username']
         password = request.form['Password']

         qry = "insert into user_registration values(NULL,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,'admin','admin')"
         val = (fname, lname, phone, dob, licence_no, house_name, place, post, city, district, state, pin, username, password)
         iud(qry, val)
         return jsonify({'task': 'success'})
     except Exception as e:
        print(e)
        return jsonify({'task': 'error'})


app.run(host='0.0.0.0',port=5000)