import connecttodatabase
import preparedata
import mlmodel
import argparse
import traceback

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Prices prediction')
    parser.add_argument('--learning_from',
                        type=str,
                        default="2017-05-18",
                        help='Date from which learning will start')
    parser.add_argument('--learning_to',
                        type=str,
                        default="2018-05-18",
                        help='Date on which learning will stop')
    parser.add_argument('--predict_to',
                        type=str,
                        default="2018-05-30",
                        help='Date on which prediction will stop')
    parser.add_argument('--commoditie',
                        type=str,
                        default="gold",
                        help='Commoditie to predict')
    parser.add_argument('--train_test_split_ratio',
                        type=float,
                        default=0.85,
                        help='Train/test split ratio')
    args = parser.parse_args()
    try:
        cursor = connecttodatabase.connect_to_database()
        print ("Connected to database.")
        trainY, trainX, testX, testY, look_back, scaler, dataset = preparedata.prepare_data(args.learning_from, args.learning_to, cursor, args.commoditie, args.train_test_split_ratio)
        model = mlmodel.create_model(look_back)
        epochs = 100
        batch_size = 1
        verbose = 2
        model = mlmodel.fit_model(model, trainX, trainY, epochs, batch_size, verbose)
        trainPredict, testPredict = mlmodel.make_prediction(model, trainX, testX)
        trainPredict, trainY, testPredict, testY = mlmodel.invert_prediction(scaler, trainPredict, testPredict, trainY, testY)
        mlmodel.calculate_error(trainY, trainPredict, testY, testPredict)
        trainPredictPlot = mlmodel.shift_train_prediction(dataset, look_back, trainPredict)
        testPredictPlot = mlmodel.shift_test_prediction(dataset, look_back, trainPredict, testPredict)
        mlmodel.plot(scaler, dataset, trainPredictPlot, testPredictPlot)
    except Exception:
        traceback.print_exc()
    