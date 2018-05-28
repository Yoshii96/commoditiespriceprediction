import pandas as pd
import numpy as np
from sklearn.preprocessing import MinMaxScaler

def create_dataset(dataset, look_back=1):
	dataX, dataY = [], []
	for i in range(len(dataset)-look_back-1):
		a = dataset[i:(i+look_back), 0]
		dataX.append(a)
		dataY.append(dataset[i + look_back, 0])
	return np.array(dataX), np.array(dataY)

def prepare_data(learnig_from, learnig_to, cursor, commoditie, split_ratio):
    records = get_data_from_database(learnig_from, learnig_to, cursor, commoditie)
    data_frame = pd.DataFrame(records, columns=['price_am', 'price_pm'])
    data_frame = pd.DataFrame(data_frame.stack(), columns=["prices"])
    print ("Learning will be performed on {} prices.".format(len(data_frame['prices'])))
    dataset = data_frame['prices'].values.astype('float32')
    scaler = MinMaxScaler(feature_range=(0, 1))
    print(dataset.shape)
    dataset = np.reshape(dataset,(len(dataset), 1))
    dataset = scaler.fit_transform(dataset)
    train_size = int(len(dataset) * split_ratio)
    train, test = dataset[0:train_size,:], dataset[train_size:len(dataset),:]
    look_back = 3
    trainX, trainY = create_dataset(train, look_back)
    testX, testY = create_dataset(test, look_back)
    trainX = np.reshape(trainX, (trainX.shape[0], 1, trainX.shape[1]))
    testX = np.reshape(testX, (testX.shape[0], 1, testX.shape[1]))
    return trainY, trainX, testX, testY, look_back, scaler, dataset

def get_data_from_database(learnig_from, learnig_to, cursor, commoditie):
    cursor.execute("SELECT price_am, price_pm \
    FROM commodities \
    WHERE type = '{}' AND\
    date >= '{}' AND\
    date <= '{}'\
    ORDER BY date;"
    .format(commoditie, learnig_from, learnig_to))
    print ("Got data from {} to {}.".format(learnig_from, learnig_to))
    return cursor.fetchall()
