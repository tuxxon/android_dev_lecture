# -*- coding: utf-8 -*-
#coding=utf-8

from flask import Flask
from flask import redirect
from flask import render_template
from flask import request
from flask import url_for
from mysql import Mysql

import json
import logging
import requests
import time
import urllib


application = Flask(__name__)

@application.route("/")
def main():
    return render_template('index.html')
    #return "<h1>Hello!</h1>"



@application.route("/users",methods=['POST'])
def insert_users():

    j = request.get_json()

    db = Mysql()
    result = db.post_users(j)
    result = {"message":"ok"} if result is None else result

    response = application.response_class(
        response=json.dumps(result),
        status=200,
        mimetype='application/json'
    )

    return response

@application.route("/users",methods=['GET'])
def list_users():

    page = int(request.args.get('page', "0"))
    np = int(request.args.get('numOfitems', "20"))

    db = Mysql()
    res = db.list_users(page=page, numOfitems=np)

    result = {
        "users" : "{}".format(res),
        "count" : len(res),
        "page"  : page
    }

    return result


@application.route("/users/<id>",methods=['GET','PUT','DELETE'])
def manage_users(id):

    if request.method == 'GET':
        result = get_users(id)
    elif request.method == 'PUT':
        result = update_users(id)
    elif request.method == 'DELETE':
        result = delete_users(id)
    else:
        result = {
            "error" : "http method not found = {}".format(request.method)
        }

    return result;


def get_users(id):

    db = Mysql()
    result = db.get_users(id)

    return json.dumps(result)


def update_users(id):

    j = request.get_json()
    db = Mysql()
    result = db.update_users(id,j)
    result = {"message":"ok"} if result is None else result

    response = application.response_class(
        response=json.dumps(result),
        status=200,
        mimetype='application/json'
    )

    return response


def delete_users(id):

    db = Mysql()
    result = db.delete_users(id)
    result = {"message":"ok"} if result is None else result
    
    response = application.response_class(
        response=json.dumps(result),
        status=200,
        mimetype='application/json'
    )

    return response


if __name__ == "__main__":
    application.run(host='0.0.0.0')
