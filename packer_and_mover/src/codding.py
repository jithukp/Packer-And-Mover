

import os
from flask import *
from werkzeug.utils import secure_filename

from src.dbcon import *
app=Flask(__name__)

app.secret_key="123"

@app.route('/')
def login():
    qry="SELECT*FROM `user_registration` WHERE `category`='admin'"
    res=selectones(qry)
    if res is None:
        res="no"
    else:
        res="yes"
    return render_template("login.html",val=res)

@app.route('/LOGIN',methods=['POST'])
def LOGIN():
    uname=request.form['textfield']
    pswd=request.form['textfield2']
    qry="select * from user_registration where username=%s and password=%s"
    val=(uname,pswd)
    res=selectone(qry,val)
    print(res)
    if res is None:
        return'''<script>alert("invalid");window.location="/"</script>'''
    elif res[15]=="admin":
        session['lid'] = res[0]
        return redirect('/ahp')
    elif res[15]=="freight worker":
        session['lid']=res[0]
        return redirect('/whp')
    else:
        return'''<script>alert("invalid");window.location="/"</script>'''



@app.route('/av')
def av():
    return render_template("add_vehicle.html")

@app.route('/ahp')
def ahp():
    return render_template("admin_home_page.html")

@app.route('/ad')
def ad():
    qry="SELECT * FROM user_registration WHERE category='driver' and status='pending'"
    res=selectall(qry)
    return render_template("approve_driver.html",val=res)

@app.route('/approve_driver')
def approve_driver():
    id=request.args.get('id')
    qry="UPDATE `user_registration` SET  status='approved' WHERE u_id =%s"
    val=(id)
    iud(qry,val)
    return redirect('/ad')

@app.route('/reject_driver')
def reject_driver():
    id = request.args.get('id')
    qry = "UPDATE `user_registration` SET `status` ='rejected' WHERE u_id =%s "
    val = (id)
    iud(qry,val)
    return redirect('/ad')

@app.route('/afw')
def afw():
    qry = "SELECT * FROM user_registration WHERE category='freight worker' and status='pending'"
    res = selectall(qry)
    return render_template("approve_freight_worker.html",val=res)

@app.route('/approve_worker')
def approve_worker():
    id=request.args.get('id')
    qry="UPDATE `user_registration` SET `status`='approved' WHERE u_id =%s"
    val=(id)
    iud(qry,val)
    return redirect('/afw')

@app.route('/reject_worker')
def reject_worker():
    id = request.args.get('id')
    qry = "UPDATE `user_registration` SET `status` ='rejected' WHERE u_id =%s "
    val = (id)
    iud(qry,val)
    return redirect('/afw')

@app.route('/avh')
def avh():
    r_id=request.args.get('id')
    session['ur_id']=r_id
    qry="SELECT `user_registration`.`u_id`,`user_registration`.`first_name`,`user_registration`.`last_name` FROM `user_registration` WHERE `category`='driver' AND `status`='approved'"
    res=selectall(qry)
    qry="SELECT * FROM `vehicle`"
    res2=selectall(qry)
    return render_template("assign _vehicle.html",val=res,val2=res2)

@app.route('/ASSIGN_VEHICLE',methods=["post"])
def ASSIGN_VEHICLE():
    vehicle = request.form['select']
    driver = request.form['select2']
    qry = "insert into work values(NULL,%s,%s,%s,'assigned')"
    val=(session['ur_id'],driver,vehicle)
    iud(qry,val)
    qry1="UPDATE `user_request` SET `status`='assigned' WHERE `ur_id`=%s"
    iud(qry1,session['ur_id'])
    return '''<script>alert("successfully assigned");window.location="/awv"</script>'''
    
@app.route('/awv')
def awv():
    qry="SELECT `user_registration`.`first_name`,`user_registration`.`last_name`,`user_registration`.`place`,`user_registration`.`u_id`,`user_request`.* FROM `user_request` inner JOIN `user_registration` ON `user_registration`.`u_id`=`user_request`.`u_id`  WHERE `user_request`.`status`='approved'"
    db=Db()
    res = db.select(qry)
    return render_template("assign_workand_vehicle.html",val=res)




@app.route('/ADD_VEHICLE',methods=["post"])
def ADD_VEHICLE():
    vtype = request.form['textfield']
    rcdetails = request.files['file2']
    file=secure_filename(rcdetails.filename)
    rcdetails.save(os.path.join('static/rcdetails',file))
    photo = request.files['file']
    fn=secure_filename(photo.filename)
    photo.save(os.path.join('static/vphoto', fn))
    qry = "insert into vehicle values(NULL,%s,%s,%s)"
    val=(vtype,file,fn)
    iud(qry,val)
    return '''<script>alert("succussfully added");window.location="/av"</script>'''

@app.route('/bd')
def bd():
    qry="SELECT `user_registration`.`first_name`,`user_registration`.`last_name`,`user_registration`.`license_no`,`user_registration`.`house_name`,`user_registration`.`place`,`user_registration`.`post`,`user_registration`.`city`,`user_registration`.`district`,`user_registration`.`state`,`user_registration`.`pin`,`user_registration`.`u_id`,`user_registration`.`status`  FROM `user_registration` where   `category`='driver'"
    res=selectall(qry)
    return render_template("block_driver.html",val=res)

@app.route('/BLOCK_DRIVER')
def BLOCK_DRIVER():
    id=request.args.get('id')
    qry="UPDATE `user_registration` SET `status`='block' WHERE u_id=%s"
    val=(id)
    iud(qry,val)
    return '''<script>alert("Blocked");window.location="/bd"  </script>'''
    # return redirect('/bd')

@app.route('/UNBLOCK_DRIVER')
def UNBLOCK_DRIVER():

    id=request.args.get('id')
    qry="UPDATE `user_registration` SET `status`='approved' WHERE u_id=%s"
    val=(id)
    iud(qry,val)
    return '''<script>alert("Unblocked");window.location="/bd"  </script>'''

@app.route('/block_worker')
def block_worker():
    qry="SELECT `user_registration`.`first_name`,`user_registration`.`last_name`,`user_registration`.`phone_no`,`user_registration`.`house_name`,`user_registration`.`place`,`user_registration`.`post`,`user_registration`.`city`,`user_registration`.`district`,`user_registration`.`state`,`user_registration`.`pin`,`user_registration`.`u_id`,`user_registration`.`status`  FROM `user_registration` WHERE   `category`='freight worker'"
    res = selectall(qry)
    return render_template("block_worker.html", val=res)
@app.route('/driver_payments')
def driver_payments():
    qry="SELECT `user_registration`.`first_name`,`last_name`,`phone_no` ,SUM(amount) FROM `payment` JOIN `work` ON `work`.`ur_id`=`payment`.`ur_id` JOIN `user_registration` ON `user_registration`.`u_id`=`work`.`u_id` GROUP BY `work`.`u_id`"
    res = selectall(qry)
    return render_template("driver_payments.html", val=res)

@app.route('/BLOCK_WORKER')
def BLOCK_WOEKER():
    id=request.args.get('id')
    qry="UPDATE `user_registration` SET `status`='block' WHERE u_id=%s"
    val=(id)
    iud(qry,val)
    return '''<script>alert("Blocked");window.location="/block_worker"  </script>'''
    # return redirect('/bd')

@app.route('/UNBLOCK_WORKER')
def UNBLOCK_WORKER():

    id=request.args.get('id')
    qry="UPDATE `user_registration` SET `status`='approved' WHERE u_id=%s"
    val=(id)
    iud(qry,val)
    return '''<script>alert("Unblocked");window.location="/block_worker"  </script>'''

@app.route('/mv')
def mv():
    return render_template("manage_vehicle.html")

@app.route('/r',methods=['post'])
def r():
    type=request.form['select']
    qry="SELECT `user_registration`.`first_name`,`user_registration`.`last_name`,`rating`.`rate` FROM `user_registration` JOIN `rating` ON `user_registration`.`u_id`=`rating`.`u_id` WHERE `category`=%s"
    val=(type)
    res=selectall2(qry,val)
    return render_template("rating_view.html",val=res)

@app.route('/rating')
def rating():
    return render_template('rating.html')


@app.route("/admin_view_payment_report", methods=['get', 'post'])
def admin_view_payment_report():
    if request.method=="POST":
        date1=request.form['date1']
        date2=request.form['date2']
        db=Db()
        res=db.select("SELECT * FROM payment WHERE DATE BETWEEN '"+date1+"' AND '"+date2+"'")
        res2=db.select("SELECT * FROM worker_bill WHERE DATE BETWEEN '"+date1+"' AND '"+date2+"'")
        dtot=0
        wtot=0
        for i in res:
            dtot=dtot+float(i['amount'])
        for j in res2:
            wtot=wtot+float(j['charge'])
        data=[]
        dat1={"particulars":'Driver Payments', 'amount':dtot}
        dat2={"particulars":'Worker Payments', 'amount':wtot}
        data.append(dat1)
        data.append(dat2)
        tot=dtot+wtot
        return render_template("admin_view_payment_report.html", date1=date1, date2=date2, dtot=dtot, wtot=wtot, tot=tot, data=data)
    return render_template("admin_view_payment_report.html")


@app.route("/admin_view_reschedule_report", methods=['get', 'post'])
def admin_view_reschedule_report():
    if request.method=="POST":
        date1=request.form['date1']
        date2=request.form['date2']
        db=Db()
        res=db.select("SELECT `resheduled_request`. *,user_registration.*,`user_request`.`date` FROM  WORK, user_registration, `resheduled_request`, `user_request` WHERE `resheduled_request`.w_id=work.w_id AND `resheduled_request`.u_id=`user_registration`.u_id AND `user_request`.ur_id=`work`.ur_id  AND `user_request`.date BETWEEN '"+date1+"' AND '"+date2+"'")
        cnt=len(res)
        print(cnt,res)

        return render_template("admin_view_reschedule_report.html", cnt=cnt, data=res, date1=date1, date2=date2)
    return render_template("admin_view_reschedule_report.html")


@app.route("/admin_view_user_request_report", methods=['get', 'post'])
def admin_view_user_request_report():
    if request.method=="POST":
        year=request.form['select']
        req_stat={"January":0, "February":0, "March":0, "April":0, "May":0, "June":0, "July":0,
                  "August":0, "September":0, "October":0, "November":0, "December":0}

        db=Db()
        tot=0
        res=db.select("SELECT * FROM user_request")
        for i in res:
            date=i['date']
            yr=date.split("-")[0]
            mnth=date.split("-")[1]
            if yr==year:
                tot=tot+1
                if mnth=="01":
                    req_stat['January']=req_stat['January']+1
                elif mnth=="02":
                    req_stat['February'] = req_stat['February'] + 1
                elif mnth=="03":
                    req_stat['March'] = req_stat['March'] + 1
                elif mnth=="04":
                    req_stat['April'] = req_stat['April'] + 1
                elif mnth=="05":
                    req_stat['May'] = req_stat['May'] + 1
                elif mnth=="06":
                    req_stat['June'] = req_stat['June'] + 1
                elif mnth=="07":
                    req_stat['July'] = req_stat['July'] + 1
                elif mnth=="08":
                    req_stat['August'] = req_stat['August'] + 1
                elif mnth=="09":
                    req_stat['September'] = req_stat['September'] + 1
                elif mnth=="10":
                    req_stat['October'] = req_stat['October'] + 1
                elif mnth=="11":
                    req_stat['November'] = req_stat['November'] + 1
                elif mnth=="12":
                    req_stat['December'] = req_stat['December'] + 1
        return render_template("admin_view_user_request_report.html", tot=tot, year=year, data=req_stat)
    return render_template("admin_view_user_request_report.html")



# @app.route('/rv',methods=['post'])
# def rv():
#     return render_template("rating_view.html")

@app.route('/adminrg')
def adminrg():
    return render_template("admin_registration.html")

@app.route('/workerrg')
def workerrg():
    return render_template("worker_registration.html")

@app.route('/REGISTRATION',methods=["post"])
def REGISTRATION():
    try:
        fname=request.form['textfield14']
        lname = request.form['textfield2']
        phone = request.form['textfield3']
        dob = request.form['textfield4']
        licence_no = request.form['textfield']
        house_name = request.form['textfield6']
        place = request.form['textfield7']
        post = request.form['textfield8']
        city = request.form['textfield9']
        district = request.form['textfield10']
        state = request.form['textfield11']
        pin = request.form['textfield12']
        username = request.form['textfield5']
        password = request.form['textfield13']

        qry="insert into user_registration values(NULL,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,'admin','admin')"
        val=(fname,lname,phone,dob,licence_no,house_name,place,post,city,district,state,pin,username,password)
        iud(qry,val)
        return '''<script>alert("successfully registered");window.location="/"</script>'''
    except Exception as e:
        return '''<script>alert("Already Exist");window.location="/"</script>'''


@app.route('/WORKER_REGISTRATION',methods=["post"])
def WORKER_REGISTRATION():
    try:
        fname=request.form['textfield14']
        lname = request.form['textfield2']
        phone = request.form['textfield3']
        dob = request.form['textfield4']
        licence_no = request.form['textfield']
        house_name = request.form['textfield6']
        place = request.form['textfield7']
        post = request.form['textfield8']
        city = request.form['textfield9']
        district = request.form['textfield10']
        state = request.form['textfield11']
        pin = request.form['textfield12']
        username = request.form['textfield5']
        password = request.form['textfield13']

        qry="insert into user_registration values(NULL,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,'freight worker','pending')"
        val=(fname,lname,phone,dob,licence_no,house_name,place,post,city,district,state,pin,username,password)
        iud(qry,val)
        return '''<script>alert("successfully registered");window.location="/"</script>'''
    except Exception as e:
        return '''<script>alert("Already Exist");window.location="/workerrg"</script>'''


@app.route('/rd')
def rd():
    rid=request.args.get('rid')
    # session['rid']=rid
    tot=0
    qry="SELECT `item`.`itemname`,`price`,`request_details`.`item_count`,`request_details`.`item_count`*price tot FROM `item` JOIN `request_details` ON `request_details`.`item_id`=`item`.`item_id` WHERE `request_details`.`ur_id`=%s"
    res=selectall2(qry,rid)
    for i in res:
        tot+=int(i[3])
    session['tot']=tot
    return render_template("request_details.html",val=res,i=rid)

@app.route('/vd')
def vd():
    rid=request.args.get('id')
    qry="SELECT `item`.`itemname`,`price`,`request_details`.`item_count`,`request_details`.`item_count`*price as tot FROM `item` inner JOIN `request_details` ON `request_details`.`item_id`=`item`.`item_id` WHERE `request_details`.`ur_id`='"+rid+"'"
    db=Db()
    res=db.select(qry)
    return render_template("view_details.html",val=res)

@app.route('/approve_user_request')
def approve_user_request():
    id=request.args.get('id')
    qry="UPDATE `user_request` SET  STATUS='approved' WHERE ur_id =%s"
    val=(id)
    iud(qry,val)
    return '''<script>alert("approved");window.location="/vru"</script>'''

@app.route('/reject_user_request')
def reject_user_request():
    id = request.args.get('id')
    qry = "UPDATE `user_request` SET `status` ='rejected' WHERE ur_id =%s "
    val = (id)
    iud(qry,val)
    return '''<script>alert("rejected");window.location="/vru"</script>'''

@app.route('/vbi')
def vbi():
    qry="SELECT `user_registration`.`first_name`,`user_registration`.`last_name`,`driver_bill`.`date`,`driver_bill`.`km`,`driver_bill`.`charge`,`driver_bill`.`status` FROM `driver_bill` JOIN `work` ON `driver_bill`.`w_id`=`work`.`w_id` JOIN `user_registration` ON `work`.`u_id`=`user_registration`.`u_id`"
    res=selectall(qry)
    return render_template("view_bill_information.html",val=res)


@app.route('/vp')
def vp():
    # qry="SELECT `user_registration`.`first_name`,`user_registration`.`last_name`,`user_registration`.`house_name`,`user_registration`.`place`,`user_registration`.`post`,`user_registration`.`city`,`user_registration`.`district`,`user_registration`.`state`,`user_registration`.`pin`,`requset_worker`.`no_of_worker_required`,`worker_bill`.`status` FROM `user_registration` JOIN `user_request` ON `user_registration`.`u_id`=`user_request`.`u_id` JOIN `requset_worker` ON `requset_worker`.`u_id`=`user_registration`.`u_id` JOIN `worker_bill` ON `worker_bill`.`wb_id`=`requset_worker`.`wr_id`"
    qry="SELECT `user_registration`.`first_name`,`user_registration`.`last_name`,`user_registration`.`house_name`,`user_registration`.`place`,`user_registration`.`post`,`user_registration`.`city`,`user_registration`.`district`,`user_registration`.`state`,`user_registration`.`pin`,`requset_worker`.`no_of_worker_required`,`worker_bill`.`status` FROM `user_registration` JOIN `requset_worker` ON `requset_worker`.`u_id`=`user_registration`.`u_id` JOIN `worker_bill` ON `worker_bill`.`wr_id`=`requset_worker`.`wr_id` AND `requset_worker`.`w_id`=%s"
    res=selectall2(qry,session['lid'])
    return render_template("view_payment.html",val=res)

@app.route('/vru')
def vru():
    qry = "SELECT `user_request`.*,`user_registration`.`first_name`,`last_name`,`user_registration`.`phone_no` FROM `user_registration` inner JOIN `user_request` ON `user_registration`.`u_id`=`user_request`.`u_id` WHERE `user_request`.`status`='pending'"
    db=Db()
    res = db.select(qry)
    print(res)
    return render_template("view_request_update.html",val=res)
    # return render_template("neww.html",val=res)

@app.route('/VIEW_USER')
def VIEW_USER():
    qry="SELECT * FROM `user_registration` WHERE `category`='user'"
    res = selectall(qry)
    return render_template("view_user.html",val=res)

@app.route("/view_approved_drivers")
def view_approved_drivers():
    db=Db()
    res=db.select("SELECT * FROM `user_registration`WHERE category='driver' AND (STATUS='approved' or status='blocked')")
    return render_template("approved_drivers.html", data=res)


@app.route("/view_approved_workers")
def view_approved_workers():
    db=Db()
    res=db.select("SELECT * FROM `user_registration`WHERE category='freight worker' AND STATUS='approved'")
    return render_template("approved_workers.html", data=res)

@app.route("/view_driver_rating/<did>")
def view_driver_rating(did):
    db=Db()
    res=db.select("SELECT `user_registration`.first_name, user_registration.last_name, rating.* FROM user_registration, rating WHERE user_registration.u_id=rating.u_id AND rating.to_id='"+did+"'")
    ar_rt = []

    for im in range(0, len(res)):
        val = str(res[im]['rate'])
        ar_rt.append(val)
    fs = "/static/star/full.jpg"
    hs = "/static/star/half.jpg"
    es = "/static/star/empty.jpg"
    arr = []

    for rt in ar_rt:
        print(rt)
        a = float(rt)

        if a == 0.0:
            print("eeeee")
            ar = [es, es, es, es, es]
            arr.append(ar)

        elif a == 0.5:
            print("heeee")
            ar = [hs, es, es, es, es]
            arr.append(ar)

        elif a == 1.0:
            print("feeee")
            ar = [fs, es, es, es, es]
            arr.append(ar)

        elif a == 1.5 :
            print("fheee")
            ar = [fs, hs, es, es, es]
            arr.append(ar)

        elif a == 2.0:
            print("ffeee")
            ar = [fs, fs, es, es, es]
            arr.append(ar)

        elif a == 2.5:
            print("ffhee")
            ar = [fs, fs, hs, es, es]
            arr.append(ar)

        elif a == 3.0:
            print("fffee")
            ar = [fs, fs, fs, es, es]
            arr.append(ar)

        elif a == 3.5:
            print("fffhe")
            ar = [fs, fs, fs, hs, es]
            arr.append(ar)

        elif a == 4.0:
            print("ffffe")
            ar = [fs, fs, fs, fs, es]
            arr.append(ar)

        elif a == 4.5:
            print("ffffh")
            ar = [fs, fs, fs, fs, hs]
            arr.append(ar)

        elif a == 5.0:
            print("fffff")
            ar = [fs, fs, fs, fs, fs]
            arr.append(ar)
        print(arr)
    # return render_template('admin/adm_view_apprating.html',data=re33,r1=ar,ln=len(ar55))
    return render_template('view_driver_rating.html', resu=res, r1=arr, ln=len(arr))

@app.route("/block_driver/<did>")
def block_driver(did):
    db=Db()
    db.update("UPDATE user_registration SET STATUS='blocked' WHERE u_id='"+did+"'")
    return redirect("/view_approved_drivers")

@app.route("/unblock_driver/<did>")
def unblock_driver(did):
    db=Db()
    db.update("UPDATE user_registration SET STATUS='approved' WHERE u_id='"+did+"'")
    return redirect("/view_approved_drivers")


@app.route('/vrus')
def vrus():
    db=Db()
    qry="SELECT `user_registration`.*, `requset_worker`.* FROM requset_worker, user_registration WHERE requset_worker.u_id=user_registration.u_id AND requset_worker.w_id='"+str(session['lid'])+"' and requset_worker.status='pending' order by wr_id desc"
    res=db.select(qry)
    return render_template("view_request_and_update_status.html", val=res)



@app.route('/vrr')
def vrr():
    db=Db()
    res=db.select("SELECT A.first_name AS afname, A.last_name AS alname, B.first_name AS bfname, B.last_name AS blname, B.phone_no AS bph, C.first_name AS cfname, C.last_name AS clname, C.phone_no AS cph, resheduled_request.reason FROM user_request, user_registration A, WORK, user_registration B, resheduled_request, user_registration C  WHERE user_request.u_id=A.u_id AND user_request.ur_id=work.ur_id AND work.u_id=B.u_id  AND work.w_id=resheduled_request.w_id AND `resheduled_request`.u_id=C.u_id order by resheduled_request.resh_id desc")
    return render_template("view_resheduled_request.html", data=res)

@app.route('/VIEW_VEHICLE')
def VIEW_VEHICLE():
    qry="SELECT * FROM `vehicle`"
    res=selectall(qry)
    return render_template("view_vehicle.html",val=res)

@app.route('/delete')
def delete():
    id=request.args.get('id')
    qry="DELETE FROM `vehicle` WHERE `v_id`=%s"
    val=(id)
    iud(qry,val)
    return '''<script>alert("Deleted");window.location="/VIEW_VEHICLE"</script>'''


@app.route('/whp')
def whp():
    return render_template("worker_home_page.html")

@app.route('/WORKER_REQUEST/<wrid>')
def WORKER_REQUEST(wrid):
    db=Db()
    db.update("UPDATE requset_worker SET STATUS='approved' WHERE wr_id='"+wrid+"'")
    return redirect("/vrus")

# @app.route('/WORKER_REQUEST/<wrid>/<wno>', methods=['get', 'post'])
# def WORKER_REQUEST(wrid, wno):
#     if request.method=="POST":
#         charge = request.form['textfield2']
#         db=Db()
#         db.update("UPDATE requset_worker SET STATUS='approved', charge='"+charge+"' WHERE wr_id='"+wrid+"'")
#         return redirect("/vrus")
#     return render_template("worker_request_reply.html",wno=wno)


@app.route("/worker_reject_request/<wrid>")
def worker_reject_request(wrid):
    db=Db()
    db.update("update requset_worker set status='rejected' where wr_id='"+wrid+"'")
    return redirect("/vrus")




@app.route('/vrus_approved')
def vrus_approved():
    db=Db()
    qry="SELECT `user_registration`.*, `requset_worker`.* FROM requset_worker, user_registration WHERE requset_worker.u_id=user_registration.u_id AND requset_worker.w_id='"+str(session['lid'])+"' and requset_worker.status='approved' order by wr_id desc"
    res=db.select(qry)
    return render_template("view_approved_worker_request.html", val=res)

@app.route("/worker_add_bill/<wr_id>", methods=['get', 'post'])
def worker_add_bill(wr_id):
    if request.method=="POST":
        charge=request.form['textfield']
        tot=session['tot']
        charge=int(tot)+int(charge)
        details=request.form['textarea']
        db=Db()
        res=db.selectOne("select * from worker_bill where wr_id='"+wr_id+"'")
        if res is None:
            db=Db()
            db.insert("insert into worker_bill(wr_id, date, time, charge, work_details, status) values('"+wr_id+"', curdate(), curtime(), '"+str(charge)+"', '"+details+"', 'pending')")
        else:
            db=Db()
            db.update("update worker_bill set charge='"+str(charge)+"', work_details='"+str(details)+"' where wr_id='"+str(wr_id)+"'")
        return redirect("/vrus_approved")
    return render_template("worker_add_bill.html")

@app.route('/worker_payments')
def worker_payments():
    qry="SELECT `user_registration`.`first_name`,`last_name`,`phone_no`,SUM(`worker_bill`.`charge`) FROM `worker_bill` JOIN `requset_worker` ON `requset_worker`.`wr_id`=`worker_bill`.`wr_id` JOIN `user_registration` ON `user_registration`.`u_id`=`requset_worker`.`w_id` GROUP BY requset_worker.w_id"
    res = selectall(qry)
    return render_template("driver_payments.html", val=res)
@app.route('/wrr',methods=["post"])
def wrr():
    num=request.form['textfield']
    charge=request.form['textfield2']
    qry="insert into requset_worker value (NULL,%s,%s,'pending',%s,%s)"
    val=(session['uid'],session['lid'],num,charge)
    iud(qry,val)
    return '''<script>alert("Replyed");window.location="/vrus"</script>'''

@app.route('/ADD_AND_MANAGE_PRICE')
def ADD_AND_MANAGE_PRICE():
    qry="SELECT * FROM `item`"
    res = selectall(qry)
    return render_template("add_and_manage_item.html",val=res)

@app.route('/addprice',methods=['post'])
def addprice():
    item_name = request.form['textfield']
    price = request.form['textfield2']
    qry="insert into `item` values(NULL,%s,%s)"
    val=(item_name,price)
    iud(qry,val)
    return'''<script>alert("submitted");window.location='/ADD_AND_MANAGE_PRICE'</script>'''

@app.route('/UPDATE_PRICE')
def UPDATE_PRICE():
    id = request.args.get('id')
    session['id'] = id
    qry="SELECT * FROM `item` WHERE `item_id`=%s"
    res = selectone(qry,id)
    return render_template("update_price.html",val=res)

@app.route('/edit',methods=['post'])
def edit():
    item_name = request.form['textfield']
    price = request.form['textfield2']

    qry = "UPDATE `item` SET `itemname`=%s,`price`=%s WHERE `item_id`=%s"
    val = (item_name, price,session['id'])
    iud(qry, val)
    return '''<script>alert("Success");window.location='/ADD_AND_MANAGE_PRICE'</script>'''


@app.route('/DELETE')
def DELETE():
    id=request.args.get('id')
    qry="DELETE FROM `item` WHERE `item_id`=%s"
    val=(id)
    iud(qry,val)
    return '''<script>alert("Deleted");window.location="/ADD_AND_MANAGE_PRICE"</script>'''


@app.route('/MANAGE_PRICE',methods=['post'])
def MANAGE_PRICE():
    qry = "SELECT * FROM `item`"
    res = selectall(qry)
    return render_template("manage_price.html",val=res)











######################                  ANDROID
from src.DBConnection import Db

@app.route("/andlogin", methods=['post'])
def andlogin():
    username=request.form['username']
    password=request.form['password']
    db=Db()
    qry = "select * from user_registration where username='"+username+"' and password='"+password+"'"
    res = db.selectOne(qry)
    if res is None:
        return jsonify(status="no")
    elif res['category']=="user":
        return jsonify(status="ok", id=res['u_id'], type=res['category'])
    elif res['category'] == "driver" and res['status']=="approved":
        return jsonify(status="ok", id=res['u_id'], type=res['category'])
    else:
        return jsonify(status="rej")


@app.route("/regform", methods=['post'])
def regform():
    fname=request.form['fname']
    lname=request.form['lname']
    phone=request.form['phone']
    dob=request.form['dob']
    lic=request.form['lic']
    hname=request.form['hname']
    place=request.form['place']
    post=request.form['post']
    city=request.form['city']
    district=request.form['district']
    state=request.form['state']
    pin=request.form['pin']
    username=request.form['username']
    pswd=request.form['pswd']
    category=request.form['category']
    db=Db()
    if category=="driver":
        stat="pending"
    elif category=="user":
        stat="approved"
    qry="INSERT INTO `user_registration` VALUES(null, '"+fname+"', '"+lname+"', '"+phone+"', '"+dob+"', '"+lic+"', " \
         "'"+hname+"', '"+place+"', '"+post+"', '"+city+"', '"+district+"', '"+state+"', '"+pin+"', '"+username+"', " \
          "'"+pswd+"', '"+category+"', '"+stat+"')"
    db.insert(qry)
    return jsonify(status="ok")


##############################          DRIVER
@app.route("/driver_view_works", methods=['post'])
def driver_view_works():
    lid=request.form['lid']
    db=Db()
    res=db.select("SELECT `user_request`.*, `user_registration`.first_name, user_registration.last_name,  user_registration.phone_no, work.w_id FROM WORK, user_request, user_registration WHERE work.ur_id=user_request.ur_id AND user_request.u_id=user_registration.u_id AND work.u_id='"+lid+"' AND work.status='assigned'")
    return jsonify(status="ok", data=res)

@app.route("/driver_view_works_more", methods=['post'])
def driver_view_works_more():
    wid=request.form['wid']
    db=Db()
    res = db.selectOne(
        "SELECT `user_request`.*, `user_registration`.*, work.w_id FROM WORK, user_request, user_registration WHERE work.ur_id=user_request.ur_id AND user_request.u_id=user_registration.u_id AND work.w_id='" + wid + "'")
    return jsonify(status="ok", name=res['first_name']+ " " + res['last_name'], phone=res['phone_no'], lati=res['latitude'],
                   address=res['house_name']+"\n"+res['place']+"\n"+res['post']+"\n"+res['city']+"\n"+res['district'],
                   logi=res['longitude'])


@app.route("/driver_add_bill", methods=['post'])
def driver_add_bill():
    wid=request.form['wid']
    dist=request.form['dist']
    charge=request.form['charge']
    db=Db()
    res=db.selectOne("select * from driver_bill where w_id='"+wid+"'")
    if res is None:
        db=Db()
        db.insert("INSERT INTO driver_bill(w_id, DATE, time, km, charge, STATUS) VALUES('"+wid+"', CURDATE(), curtime(), '"+dist+"', '"+charge+"', 'pending')")
    else:
        db=Db()
        db.update("update driver_bill set km='"+dist+"', charge='"+charge+"', date=curdate(), time=curtime() where w_id='"+wid+"'")

    db=Db()
    res = db.selectOne(
        "SELECT `user_request`.*, `user_registration`.*, work.w_id FROM WORK, user_request, user_registration WHERE work.ur_id=user_request.ur_id AND user_request.u_id=user_registration.u_id AND work.w_id='" + wid + "'")
    ur_id=str(res['ur_id'])
    import pyqrcode
    big_code = pyqrcode.create(wid+"#"+ur_id, error='L', version=27, mode='binary')
    # qr=qrcode.make(wid+"#"+ur_id)
    # qr.save(r'D:\regional_works\packer_and_mover\packer_and_mover\src\static\qr_imgs\\'+wid+".png")
    big_code.png(r'C:\Users\JITHU\Downloads\Donbosco\packer_and_mover\src\static\qr_imgs\\'+wid+".png", scale=6, module_color=[0, 0, 0, 128], background=[0xff, 0xff, 0xcc])
    path="/static/qr_imgs/"+wid+".png"
    return jsonify(status="ok", path=path)

@app.route("/and_loc_update", methods=['post'])
def and_loc_update():
    lid=request.form['lid']
    lati=request.form['lati']
    logi=request.form['logi']
    db=Db()
    res=db.selectOne("SELECT * FROM location WHERE u_id='"+lid+"'")
    if res is None:
        db=Db()
        db.insert("INSERT INTO location(u_id, latitude, longitude) VALUES('"+lid+"', '"+lati+"', '"+logi+"')")
    else:
        db=Db()
        db.update("UPDATE location SET latitude='"+lati+"', longitude='"+logi+"' WHERE u_id='"+lid+"'")
    return jsonify(status="ok")

@app.route("/and_driver_reschedule", methods=['post'])
def and_driver_reschedule():
    wid=request.form['wid']
    reason=request.form['reason']
    lid=request.form['lid']
    db=Db()
    db.insert("INSERT INTO `resheduled_request`(w_id, u_id, reason, STATUS) VALUES('"+wid+"', '"+lid+"', '"+reason+"', 'pending')")
    res=db.selectOne("SELECT * FROM WORK INNER JOIN `user_request` ON work.ur_id=`user_request`.ur_id WHERE work.w_id='"+wid+"'")
    lati=res['latitude']
    logi=res['longitude']
    qry = "SELECT location.*, SQRT( POW(69.1 * (latitude - '" + lati + "'), 2) +POW(69.1 * ('" + logi + "' - longitude) * COS(latitude/ 57.3), 2)) AS distance FROM location where u_id!='"+lid+"' ORDER BY distance"

    #   get  nearest driver
    db=Db()
    res2=db.selectOne(qry)
    uid=res2['u_id']

    #   assigning to nearest driver
    db=Db()
    db.update("UPDATE WORK SET u_id='"+str(uid)+"', status='rescheduled' WHERE w_id='"+wid+"'")
    return jsonify(status="ok")

@app.route("/driver_view_rescheduled_works", methods=['post'])
def driver_view_rescheduled_works():
    lid=request.form['lid']
    db=Db()
    res=db.select("SELECT `user_request`.*, `user_registration`.first_name, user_registration.last_name,  user_registration.phone_no, work.w_id FROM WORK, user_request, user_registration WHERE work.ur_id=user_request.ur_id AND user_request.u_id=user_registration.u_id AND work.u_id='"+lid+"' AND work.status='rescheduled'")
    return jsonify(status="ok", data=res)


@app.route("/driver_view_payment", methods=['post'])
def driver_view_payment():
    lid=request.form['lid']
    db=Db()
    res=db.select("SELECT payment.date, payment.amount, user_registration.first_name, user_registration.last_name, user_registration.phone_no FROM payment, user_request, user_registration, WORK WHERE payment.ur_id=user_request.ur_id AND user_request.ur_id=work.ur_id AND user_request.u_id=user_registration.u_id AND work.u_id='"+lid+"' AND work.status='completed' ORDER BY payment.payment_id DESC")
    print("SELECT payment.date, payment.amount, user_registration.first_name, user_registration.last_name, user_registration.phone_no FROM payment, user_request, user_registration, WORK WHERE payment.ur_id=user_request.ur_id AND user_request.ur_id=work.ur_id AND user_request.u_id=user_registration.u_id AND work.u_id='"+lid+"' AND work.status='completed' ORDER BY payment.payment_id DESC")
    return jsonify(status="ok", data=res)





##########################      USER

@app.route("/and_view_items", methods=['post'])
def and_view_items():
    db=Db()
    res=db.select("select * from item")
    return jsonify(status="ok", data=res)


@app.route("/user_add_items", methods=['post'])
def user_add_items():
    lid=request.form['lid']
    itemid=request.form['itemid']
    qty=request.form['qty']
    lati=request.form['lati']
    logi=request.form['logi']
    db=Db()
    res=db.selectOne("SELECT * FROM `user_request` WHERE u_id='"+lid+"' AND STATUS='add_to_cart'")
    if res is None:
        db=Db()
        rid=db.insert("INSERT INTO `user_request`(u_id, DATE, latitude, longitude, STATUS) VALUES('"+lid+"', CURDATE(), '"+lati+"', '"+logi+"', 'add_to_cart')")
        print("yy",rid)
    else:
        rid=res['ur_id']
    db=Db()
    db.insert("INSERT INTO `request_details`(ur_id, item_id, item_count) VALUES('"+str(rid)+"', '"+itemid+"', '"+qty+"')")
    return jsonify(status="ok")

@app.route("/and_view_cart", methods=['post'])
def and_view_cart():
    lid=request.form['lid']
    db=Db()
    res=db.select("SELECT request_details.*, item.itemname, request_details.item_count*item.price AS amount FROM `request_details`, item, user_request WHERE request_details.item_id=item.item_id AND request_details.ur_id=`user_request`.ur_id AND user_request.u_id='"+lid+"' AND user_request.status='add_to_cart'")
    tot=0
    for i in res:
        tot=tot+float(i['amount'])
    return jsonify(status="ok", data=res, amt=str(tot))

@app.route("/and_delete_cart", methods=['post'])
def and_delete_cart():
    rd_id=request.form['rd_id']
    db=Db()
    db.delete("DELETE FROM request_details WHERE rd_id='"+rd_id+"'")
    return jsonify(status="ok")

@app.route("/and_book_request", methods=['post'])
def and_book_request():
    lid=request.form['lid']
    amt=request.form['amt']
    lati=request.form['lati']
    logi=request.form['logi']
    amt=amt.split(":")[-1]
    amt=amt.strip(" ")
    db=Db()
    db.update("UPDATE `user_request` SET STATUS='pending', latitude='"+lati+"', longitude='"+logi+"', amount='"+amt+"' WHERE u_id='"+lid+"' AND STATUS='add_to_cart'")
    cnt=db.selectOne("SELECT ur_id FROM  `user_request` WHERE  STATUS='pending' AND latitude='"+lati+"' AND longitude='"+logi+"' AND  amount='"+amt+"' AND u_id='"+lid+"' ")
    print("ouu",cnt['ur_id'])
    return jsonify(status="ok",cnt=cnt['ur_id'])

@app.route("/and_view_request", methods=['post'])
def and_view_request():
    lid=request.form['lid']
    db=Db()
    res=db.select("SELECT * FROM user_request WHERE u_id='"+lid+"' ORDER BY ur_id DESC")
    return jsonify(status="ok", data=res)

@app.route("/and_view_request_items", methods=['post'])
def and_view_request_items():
    rd_id=request.form['rd_id']
    db=Db()
    res=db.select("SELECT request_details.*, item.itemname, request_details.item_count*item.price AS amount FROM `request_details`, item WHERE request_details.item_id=item.item_id AND request_details.ur_id='"+rd_id+"'")
    res2=db.selectOne("SELECT * FROM WORK, user_registration where user_registration.u_id=work.u_id AND work.ur_id='"+rd_id+"'")
    if res2 is None:
        dname="Not assigned"
        dphone="Not available"
        stat="no"
        lati=""
        logi=""
        did=""
    else:
        res3 = db.selectOne(
            "SELECT * FROM WORK, location, user_registration where user_registration.u_id=work.u_id and location.u_id=work.u_id AND work.ur_id='" + rd_id + "'")
        if res3 is not None:
            dname=res2['first_name'] + " " +res2['last_name']
            dphone=res2['phone_no']
            stat="yes"
            lati=res3['latitude']
            logi=res3['longitude']
            did=res2['u_id']
        else:
            lati = ""
            logi = ""
            did = res2['u_id']
            dname = res2['first_name'] + " " + res2['last_name']
            dphone = res2['phone_no']
            stat = "yes1"

    return jsonify(status="ok", data=res, dname=dname, dphone=dphone, stat=stat, lati=lati, logi=logi, did=did)

@app.route("/and_send_rating", methods=['post'])
def and_send_rating():
    lid=request.form['lid']
    did=request.form['did']
    rating=request.form['rate']
    rtype=request.form['rtype']
    db=Db()
    res=db.selectOne("SELECT * FROM rating WHERE u_id='"+lid+"' AND to_id='"+did+"'")
    if res is None:
        db=Db()
        db.insert("INSERT INTO rating(u_id, to_id, rate, TYPE) VALUES('"+lid+"', '"+did+"', '"+rating+"', '"+rtype+"')")
    else:
        db=Db()
        db.update("update rating set rate='"+rating+"' where u_id='"+lid+"' and to_id='"+did+"'")
    return jsonify(status="ok")


@app.route("/and_scan_driver_qr", methods=['post'])
def and_scan_driver_qr():
    lid=request.form['lid']
    qr=request.form['qr']
    rd_id=request.form['rd_id']
    wid=qr.split("#")[0]
    ur_id=qr.split("#")[1]
    if rd_id!=ur_id:                    # qr code does not match selected request
        print("QR not matching")
        return jsonify(status="no")
    else:
        print("QR match")
        return jsonify(status="ok", wid=wid)

@app.route("/and_view_driver_bill", methods=['post'])
def and_view_driver_bill():
    wid=request.form['wid']
    db=Db()
    res=db.selectOne("SELECT * FROM WORK, user_request WHERE user_request.ur_id=work.ur_id AND work.w_id='"+wid+"'")
    res2=db.selectOne("SELECT * FROM driver_bill WHERE w_id='"+wid+"'")
    tot=float(res['amount'])+float(res2['charge'])
    return jsonify(status="ok", date=res['date'], amt=res['amount'], bdate=res2['date'], btime=res2['time'],
                   dist=res2['km'], charge=res2['charge'], tot=tot)

@app.route("/and_payment", methods=['post'])
def and_payment():
    lid=request.form['lid']
    bank=request.form['bank']
    acc=request.form['acc']
    ifsc=request.form['ifsc']
    ur_id=request.form['ur_id']
    wid=request.form['wid']
    db=Db()
    res=db.selectOne("select * FROM user_request WHERE ur_id='" + ur_id + "'")
    amt=res['amount']
    res3=db.selectOne("select * from driver_bill where w_id='"+wid+"'")
    charge=res3['charge']
    tot=(float(amt)+float(charge))
    db=Db()
    res2=db.selectOne("SELECT * FROM bank WHERE u_id='"+lid+"' AND bank='"+bank+"' AND ifsc_code='"+ifsc+"' AND account_no='"+acc+"'")
    if res2 is None:
        return jsonify(status="no")
    else:
        if tot>float(res2['balance']):
            return jsonify(status="insuff")
        else:
            db=Db()
            db.update("update user_request set status='delivered' where ur_id='"+ur_id+"'")
            db.update("update work set status='completed' where w_id='"+wid+"'")
            db.insert("INSERT INTO payment(DATE, ur_id, acc_no, amount) VALUES(CURDATE(), '"+ur_id+"', '"+acc+"', '"+str(tot)+"')")
            db.update("update bank set balance=balance-'"+str(tot)+"' where p_id='"+str(res2['p_id'])+"'")
            db.update("UPDATE driver_bill SET STATUS='paid' WHERE w_id='"+wid+"'")
            return jsonify(status="ok")

@app.route("/and_view_workers", methods=['post'])
def and_view_workers():
    db=Db()
    res=db.select("SELECT * FROM user_registration WHERE category='freight worker' AND STATUS='approved'")
    return jsonify(status="ok", data=res)

@app.route("/and_send_request", methods=['post'])
def and_send_request():
    lid=request.form['lid']
    wid=request.form['wid']
    wno=request.form['wno']
    reqid = request.form['reqid']
    db=Db()
    db.insert("INSERT INTO `requset_worker`(wr_id,u_id,w_id, STATUS, no_of_worker_required) VALUES('"+reqid+"','"+lid+"', '"+wid+"', 'pending', '"+wno+"')")
    return jsonify(status="ok")

@app.route("/and_view_worker_request", methods=['post'])
def and_view_woker_request():
    lid=request.form['lid']
    db=Db()
    res=db.select("SELECT requset_worker.*, user_registration.first_name, user_registration.last_name, user_registration.phone_no  FROM requset_worker, user_registration WHERE requset_worker.w_id=user_registration.u_id AND requset_worker.u_id='"+lid+"'")
    return jsonify(status='ok', data=res)

@app.route("/and_view_worker_bill", methods=['post'])
def and_view_worker_bill():
    wr_id=request.form['wr_id']
    print("Wr  ", wr_id)
    db=Db()
    res=db.selectOne("SELECT * FROM `worker_bill` WHERE wr_id='"+wr_id+"'")
    if res is not None:
        return jsonify(status="ok", date=res['date'], time=res['time'], charge=res['charge'], work_details=res['work_details'])
    else:
        return jsonify(status="no")


@app.route("/and_payment2", methods=['post'])
def and_payment2():
    lid=request.form['lid']
    bank=request.form['bank']
    acc=request.form['acc']
    ifsc=request.form['ifsc']
    wr_id=request.form['wr_id']
    db=Db()

    res3=db.selectOne("select * from worker_bill where wr_id='"+wr_id+"'")
    charge=res3['charge']

    db=Db()
    res2=db.selectOne("SELECT * FROM bank WHERE u_id='"+lid+"' AND bank='"+bank+"' AND ifsc_code='"+ifsc+"' AND account_no='"+acc+"'")
    if res2 is None:
        return jsonify(status="no")
    else:
        if float(charge)>float(res2['balance']):
            return jsonify(status="insuff")
        else:
            db=Db()

            db.update("update bank set balance=balance-'"+str(charge)+"' where p_id='"+str(res2['p_id'])+"'")
            db.update("UPDATE worker_bill SET STATUS='paid' WHERE wr_id='"+wr_id+"'")
            db.update("update requset_worker set status='completed' where wr_id='"+wr_id+"'")
            return jsonify(status="ok")



app.run(host='0.0.0.0', port=4000)