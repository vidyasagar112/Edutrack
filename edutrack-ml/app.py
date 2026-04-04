# app.py

from flask import Flask
from flask_cors import CORS
from routes.analytics import analytics_bp
from config import Config

app = Flask(__name__)
CORS(app)

# register blueprint with URL prefix
app.register_blueprint(analytics_bp, url_prefix="/api/ml")

if __name__ == "__main__":
    print("=" * 50)
    print("  EduTrack ML Service Starting...")
    print("  Port    : 5000")
    print("  Swagger : Not needed (called by Spring Boot)")
    print("=" * 50)
    app.run(
        host=Config.HOST,
        port=Config.PORT,
        debug=Config.DEBUG
    )