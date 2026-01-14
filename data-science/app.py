from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import joblib
import numpy as np

models = {
    "pt": joblib.load("models/sentiment_pt.joblib"),
    "en": joblib.load("models/sentiment_en.joblib"),
    "es": joblib.load("models/sentiment_es.joblib"),
}
app = FastAPI()

class TextInput(BaseModel):
    text: str
    language: str  


def predict_with_features(model, text: str, top_n: int = 3):
    probs = model.predict_proba([text])[0]

    prob_negativo = probs[0]
    prob_positivo = probs[1]

    if prob_positivo >= 0.5:
        label = "positivo"
        prob = prob_positivo
    else:
        label = "negativo"
        prob = prob_negativo

    # explicabilidade
    vectorizer = model.named_steps["tfidf"]
    classifier = model.named_steps["model"]

    feature_names = np.array(vectorizer.get_feature_names_out())
    coef = classifier.coef_[0]

    text_tfidf = vectorizer.transform([text]).toarray()[0]
    contributions = text_tfidf * coef

    top_idx = np.argsort(np.abs(contributions))[-top_n:]
    top_features = feature_names[top_idx].tolist()

    return label, float(prob), top_features

@app.post("/predict")
def predict_sentiment(input: TextInput):

    if len(input.text.strip()) < 5:
        raise HTTPException(
            status_code=400,
            detail="Texto deve ter pelo menos 5 caracteres"
        )

    lang = input.language.lower()

    if lang not in models:
        raise HTTPException(
            status_code=400,
            detail="Idioma não suportado. Use: pt, en ou es"
        )

    model = models[lang]

    previsao, probabilidade, top_features = predict_with_features(
        model,
        input.text
    )

    return {
        "previsao": previsao.capitalize(),
        "probabilidade": round(probabilidade, 2),
        "idioma": lang,
        "top_features": top_features
    }
