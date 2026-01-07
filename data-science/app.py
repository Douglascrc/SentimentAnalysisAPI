from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import joblib
import numpy as np

# carregar pipeline treinado
model = joblib.load("models/sentiment_pipeline (1).joblib")

app = FastAPI()

class TextInput(BaseModel):
    text: str


def predict_with_features(text: str, top_n: int = 3):
    # probabilidades
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

    # pegar as palavras mais influentes do texto
    top_idx = np.argsort(np.abs(contributions))[-top_n:]
    palavras_chave = feature_names[top_idx].tolist()

    return label, float(prob), palavras_chave


@app.post("/predict")
def predict_sentiment(input: TextInput):

    if len(input.text.strip()) < 5:
        raise HTTPException(
            status_code=400,
            detail="Texto deve ter pelo menos 5 caracteres"
        )

    previsao, probabilidade, palavras_chave = predict_with_features(input.text)

    return {
        "previsao": previsao.capitalize(),
        "probabilidade": round(probabilidade, 2),
        "palavras_chave": palavras_chave
    }
