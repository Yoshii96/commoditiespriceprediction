import psycopg2
import sys
 
def connect_to_database():
	#Define our connection string
	conn_string = "host='localhost' dbname='commoditiespriceprediction' user='postgres' password='postgres'"
 
	# get a connection, if a connect cannot be made an exception will be raised here
	conn = psycopg2.connect(conn_string)
 
	# conn.cursor will return a cursor object, you can use this cursor to perform queries
	cursor = conn.cursor()
	return cursor
 
if __name__ == "__main__":
	connect_to_database()