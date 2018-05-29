import connecttodatabase
import preparedata
import mlmodel
import argparse
import traceback

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Prices prediction')
    parser.add_argument('--db_conf',
                        type=str,
                        required=True,
                        help='Database configuration file')
    parser.add_argument('--from_date',
                        type=str,
                        default="2017-05-18",
                        help='Date from which analysis will start')
    parser.add_argument('--to_date',
                        type=str,
                        default="2018-05-18",
                        help='Date on which analysis will stop')
    parser.add_argument('--commoditie',
                        type=str,
                        default="gold",
                        help='Commoditie to predict')
    parser.add_argument('--train_test_split_ratio',
                        type=float,
                        default=0.7,
                        help='Train/test split ratio')
    parser.add_argument('--look_back',
                        type=int,
                        default=5,
                        help='Prices to analize')
    parser.add_argument('--epochs',
                        type=int,
                        default=100,
                        help='Number of epochs')
    parser.add_argument('--batch_size',
                        type=int,
                        default=1,
                        help='Number of samples that going to be propagated through the network')
    args = parser.parse_args()


    try:
        db_conf = args.db_conf
        from_date = args.from_date
        to_date = args.to_date
        commoditie = args.commoditie
        train_test_split_ratio = args.train_test_split_ratio
        look_back = args.look_back
        epochs = args.epochs
        batch_size = args.batch_size
        cursor = connecttodatabase.connect_to_database(db_conf)
        print ("Connected to database.")
        trainY, trainX, testX, testY, scaler, dataset = preparedata.prepare_data(from_date, to_date, cursor, commoditie, train_test_split_ratio, look_back)
        print ("Data prepared.")
        model = mlmodel.create_model(look_back)
        print ("Model created.")
        model = mlmodel.fit_model(model, trainX, trainY, epochs, batch_size)
        print ("Fiting completed.")
        trainPredict, testPredict = mlmodel.make_prediction(model, trainX, testX)
        print ("Prediction completed.")
        trainPredict, trainY, testPredict, testY = mlmodel.invert_prediction(scaler, trainPredict, testPredict, trainY, testY)
        mlmodel.calculate_error(trainY, trainPredict, testY, testPredict)
        trainPredictPlot = mlmodel.shift_train_prediction(dataset, look_back, trainPredict)
        testPredictPlot = mlmodel.shift_test_prediction(dataset, look_back, trainPredict, testPredict)
        mlmodel.plot(scaler, dataset, trainPredictPlot, testPredictPlot)
    except Exception:
        traceback.print_exc()
    