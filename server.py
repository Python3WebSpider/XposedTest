from flask import Flask, request, jsonify
from loguru import logger

app = Flask(__name__)


@app.route('/data', methods=['POST'])
def receive():
    data = request.form.get('data')
    logger.debug(f'received {data}')
    return jsonify(status='success')


if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')
