import psycopg2
import sys
 
def connect_to_database(db_conf):
    #Define our connection string
    with open(db_conf, 'r') as f:
        url_line = f.readline()
        url = url_line.split(':')
        url_host = url[3][2:]
        url_db = url[4].split('/')[1][:-1]
        user_line = f.readline()
        user = user_line.split(':')[1][:-1]
        password_line = f.readline()
        password = password_line.split(':')[1][:-1]

    conn_string = "host='" + url_host + "' dbname='" + url_db + "' user='" + user + "' password='" + password + "'"
 
    # get a connection, if a connect cannot be made an exception will be raised here
    conn = psycopg2.connect(conn_string)
 
    # conn.cursor will return a cursor object, you can use this cursor to perform queries
    cursor = conn.cursor()
    return cursor
 