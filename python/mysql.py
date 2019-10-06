import config_mysql
import json
import pymysql
import utils

class Mysql:

    def __init__(self):
        try:
            self.con = pymysql.connect(
                host=config_mysql.HOST,
                port=config_mysql.PORT,
                user=config_mysql.USER,
                passwd=config_mysql.PASSWORD,
                db=config_mysql.DB,
                charset=config_mysql.CHARSET
                )
            self.cur = self.con.cursor(pymysql.cursors.DictCursor)
        except Exception as e:
            print(e)
            self.con = pymysql.connect(
                host=config_mysql.HOST,
                port=config_mysql.PORT,
                user=config_mysql.USER,
                passwd=config_mysql.PASSWORD,
                db=config_mysql.DB,
                charset=config_mysql.CHARSET
                )
            self.cur = self.con.cursor(pymysql.cursors.DictCursor)


    def __del__(self):

        self.cur.close()
        self.con.close()


    def get_users(self, id):

        sql = "SELECT id,useremail,username,userphone,userdesc,views "
        sql = sql + "FROM user "
        sql = sql + "WHERE id={};".format(id)

        print("DEBUG SQL ===>{}".format(sql))

        result = ()
        try:
            self.cur.execute(sql)
            result = self.cur.fetchall()
        except Exception as e:
            return {"error" : "{}".format(e)}

        result = {} if len(result) == 0 else result[0]

        return result


    def list_users(self, page=0, numOfitems=20):

        page = page * numOfitems;
        sql = "SELECT id,useremail,username,userphone,userdesc,views "
        sql = sql + "FROM user "
        sql = sql + "LIMIT {page},{numOfitems};".format(page=page,numOfitems=numOfitems)

        print("DEBUG SQL ===>{}".format(sql))

        self.cur.execute(sql)
        result = self.cur.fetchall()
        for row in result:
            print(row)

        return result


    def post_users(self, j):

        sql = "INSERT INTO user(useremail,username,userphone,userdesc) "
        sql = sql + "values('{useremail}','{username}','{userphone}','{userdesc}')".format(
            useremail = utils.addslashes( json.dumps(j.get("useremail",""))),
            username = utils.addslashes( json.dumps(j.get("username",""))),
            userphone = utils.addslashes( json.dumps(j.get("userphone",""))),
            userdesc = utils.addslashes( json.dumps(j.get("userdesc","")))
            )

        print("DEBUG SQL ===>{}".format(sql))
        result = None
        try:
            self.cur.execute(sql)
            self.con.commit()
        except Exception as e:
            result = {"error" : "{}".format(e)}

        return result


    def update_users(self,id, j):

        useremail = j.get("useremail","")
        username = j.get("username","")
        userphone = j.get("userphone","")
        userdesc = j.get("userdesc","")

        sql = "UPDATE user SET "
        if len(useremail) > 0:
            sql = sql + " useremail = '{}', ".format(useremail)
        if len(username) > 0:
            sql = sql + " username = '{}', ".format(username)
        if len(userphone) > 0:
            sql = sql + " userphone = '{}', ".format(userphone)
        if len(userdesc) > 0:
            sql = sql + " userdesc = '{}' ".format(userdesc)
        sql = sql + " views = views "
        sql = sql + " WHERE id = {} ".format(id)

        print("DEBUG SQL ===>{}".format(sql))
        result = None
        try:
            self.cur.execute(sql)
            self.con.commit()
        except Exception as e:
            result = {"error" : "{}".format(e)}


    def delete_users(self, id):

        sql = "DELETE FROM user "
        sql = sql + " WHERE id = '{}' ".format(id)

        print("DEBUG SQL ===>{}".format(sql))
        result = None
        try:
            self.cur.execute(sql)
            self.con.commit()
        except Exception as e:
            result = {"error" : "{}".format(e)}

