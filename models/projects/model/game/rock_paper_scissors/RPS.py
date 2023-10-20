# The example function below keeps track of the opponent's history and plays whatever the opponent played two plays ago. It is not a very good player so you will need to change the code to pass the challenge.

temp = {}


def player(prev_play, opponent_history=[]):

    global temp
    guess = "R"
    num = 6

    if prev_play in ["R", "P", "S"]:
        opponent_history.append(prev_play)

    if len(opponent_history) > num:
        inp = "".join(opponent_history[-num:])

        if "".join(opponent_history[-(num + 1):]) in temp.keys():
            temp["".join(opponent_history[-(num + 1):])] += 1
        else:
            temp["".join(opponent_history[-(num + 1):])] = 1

        possible = [inp + "R", inp + "P", inp + "S"]

        for i in possible:
            if not i in temp.keys():
                temp[i] = 0

        predict = max(possible, key=lambda key: temp[key])

        ideal_response = {'P': 'S', 'R': 'P', 'S': 'R'}

        guess = ideal_response[predict[-1]]

    return guess