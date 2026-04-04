# config.py

class Config:
    # Flask settings
    DEBUG = True
    PORT = 5000
    HOST = "0.0.0.0"

    # Spring Boot backend URL
    SPRING_BOOT_URL = "http://localhost:8080"

    # ML Model settings
    MIN_ATTEMPTS_FOR_PREDICTION = 2
    WEAK_AREA_THRESHOLD = 50.0
    STRONG_AREA_THRESHOLD = 75.0

    # Dropout risk thresholds
    HIGH_RISK_DAYS_INACTIVE = 7
    MEDIUM_RISK_DAYS_INACTIVE = 3
    HIGH_RISK_PROGRESS_THRESHOLD = 20