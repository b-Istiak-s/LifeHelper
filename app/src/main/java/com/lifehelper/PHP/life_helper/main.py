from flask import Flask, jsonify, request
from PIL import Image
import glob
import imagehash
from io import BytesIO
import requests

app = Flask(__name__)


@app.route('/chooser', methods=['GET'])
def helloworld():
    if request.method == 'GET':
        me = request.args['name']
        my_image = request.args['my_image']
        opposite_names = request.args['opposite_name'].split(",")
        girls = request.args['pic'].split(",")

        my_hash = imagehash.average_hash(Image.open('http://localhost/life_helper/users/' + me + '/' + my_image))
        selected = girls[0]
        # opposite_name_selected = opposite_names[0]
        accepted_diff = 1000
        for x in range(len(girls)-1):
            girl_hash = imagehash.average_hash(Image.open('http://localhost/life_helper/users/' + opposite_names[x] + '/' + girls[x]))
            diff = girl_hash - my_hash
            if diff < accepted_diff:
                # opposite_name_selected = opposite_names[x]
                selected = girls[x]
                selected_int = x
                accepted_diff = diff
        # bf_img = Image.open('./boys/captain.jpg')
        # gf_img = Image.open('./'+opposite_name_selected+'/'+selected)
        # couple_img = Image.new('RGB',(bf_img.width+gf_img.width,bf_img.height))
        # couple_img.paste(bf_img,(0,0))
        # couple_img.paste(gf_img,(bf_img.width,0))
        # couple_img.save('my_valentine_day_date.jpg')
        # couple_img.show()
        data = {"selection": opposite_names[selected_int]}
        return jsonify(data)


if __name__ == '__main__':
    app.run(debug=True)
