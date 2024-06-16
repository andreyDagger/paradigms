(load-file "proto.clj")
(use '[clojure.string :only (join split)])

(defn expCalc [arg]
    (Math/exp arg)
)

(defn sumexpCalc [& arr]
    (apply + (map #(Math/exp %) arr))
)

(defn lseCalc [& arr]
    (Math/log (apply sumexpCalc arr))
)

(defn sqSumCalc [& arr]
    (apply + (map #(* % %) arr))
)

(defn meansqCalc [& arr]
    (/ (apply sqSumCalc arr) (count arr))
)

(defn rmsCalc [& arr]
    (Math/sqrt (apply meansqCalc arr))
)

(defn myDiv [& args]
    (if (= (count args) 1)
        (/ 1.0 (first args))
        (/ (first args) (double (apply * (rest args))))
    )
)

(def _evaluate (method :evaluate))
(def _diff (method :diff))
(def _toString (method :toString))
(def _value (field :value))
(def _varName (field :varName))
(def _stringRepr (field :stringRepr))
(def _args (field :args))
(def _calcRule (field :calcRule))
(def _diffRule (field :diffRule))


(def AbstractOperationProto
    {:toString (fn [this]
        (str "(" (_stringRepr this) " " (join " " (map #(_toString %) (_args this) ) ) ")")
     )
     :evaluate (fn [this variablesMap]
        (apply (_calcRule this) (map #(_evaluate % variablesMap) (_args this) ) )
      )
     :diff (fn [this diffVar]
        ((_diffRule this) diffVar (_args this) )
      )
    }
)

(defn AbstractOperationCtor [this stringRepr calcRule diffRule args]
    (assoc this :stringRepr stringRepr :calcRule calcRule :diffRule diffRule :args args)
)

(declare addDiff subtractDiff multiplyDiff divideDiff negateDiff sumexpDiff lseDiff expDiff)

(defn AddCtor [this & args]
    (AbstractOperationCtor this "+" + addDiff args)
)
(def Add (constructor AddCtor AbstractOperationProto))

(defn SubtractCtor [this & args]
    (AbstractOperationCtor this "-" - subtractDiff args)
)
(def Subtract (constructor SubtractCtor AbstractOperationProto))

(defn MultiplyCtor [this & args]
    (AbstractOperationCtor this "*" * multiplyDiff args)
)
(def Multiply (constructor MultiplyCtor AbstractOperationProto))

(defn DivideCtor [this & args]
    (AbstractOperationCtor this "/" myDiv divideDiff args)
)
(def Divide (constructor DivideCtor AbstractOperationProto))

(defn NegateCtor [this arg]
    (AbstractOperationCtor this "negate" - negateDiff [arg])
)
(def Negate (constructor NegateCtor AbstractOperationProto))

(defn SumexpCtor [this & args]
    (AbstractOperationCtor this "sumexp" sumexpCalc sumexpDiff args)
)
(def Sumexp (constructor SumexpCtor AbstractOperationProto))

(defn LSECtor [this & args]
    (AbstractOperationCtor this "lse" lseCalc lseDiff args)
)
(def LSE (constructor LSECtor AbstractOperationProto))

(defn ExpCtor [this arg]
    (AbstractOperationCtor this "exp" expCalc expDiff [arg])
)
(def Exp (constructor ExpCtor AbstractOperationProto))



(defn addDiff [diffVar args]
    (apply Add (map #(_diff % diffVar) args))
)
(defn subtractDiff [diffVar args]
    (apply Subtract (map #(_diff % diffVar) args))
)
(defn multiplyDiff [diffVar args]
    (apply Add (map #(Multiply (_diff % diffVar) (Divide (apply Multiply args) %) ) args) )
)
(defn divideDiff [diffVar args]
    (if (= (count args) 1)
        (Divide (Negate (_diff (first args) diffVar )) (Multiply (first args) (first args) ) )
        (let [denom (apply Multiply (rest args) )]
            (Divide (Subtract (Multiply (_diff (first args) diffVar) denom) (Multiply (_diff denom diffVar) (first args) ) ) (Multiply denom denom))
        )
    )
)
(defn negateDiff [diffVar args]
    (Negate (_diff (first args) diffVar))
)
(defn sumexpDiff [diffVar args]
    (apply Add (map #(Multiply (Exp %) (_diff % diffVar) ) args))
)
(defn lseDiff [diffVar args]
    (Divide (sumexpDiff diffVar args) (apply Sumexp args) )
)
(defn exprDiff [diffVar arg]
    (Multiply (Exp arg) (_diff arg diffVar))
)

(declare Constant)
(def ConstantProto
    {:toString (fn [this] (str (_value this)))
     :evaluate (fn [this variablesMap] (_value this))
     :diff (fn [this diffVar] (Constant 0) )
    }
)
(defn ConstantCtor [this value]
    (assoc this :value value)
)
(def Constant (constructor ConstantCtor ConstantProto))

(def ZERO (Constant 0))
(def ONE (Constant 1))

(def VariableProto
    {:toString (fn [this] (_varName this))
     :evaluate (fn [this variablesMap] (get variablesMap (_varName this)))
     :diff (fn [this diffVar]
                (if (= diffVar (_varName this))
                    ONE
                    ZERO
                )
           )
    }
)
(defn VariableCtor [this varName]
    (assoc this :varName varName)
)
(def Variable (constructor VariableCtor VariableProto))

(def X (Variable "x"))
(def Y (Variable "Y"))
(def Z (Variable "Z"))



(def constant constantly)

(defn variable [varName]
    (fn [variablesMap]
        (get variablesMap varName)
    )
)

(defn operation [op args]
    (fn [variablesMap]
        (apply op (map #(% variablesMap) args))
    )
)

(defn negate [expr] (operation - [expr]))
(defn inverse [expr] (operation myDiv [expr]))
(defn add [& args] (operation + args))
(defn subtract [& args]
    (operation - args)
)
(defn multiply [& args] (operation * args))
(defn divide [& args]
    (operation myDiv args)
)
(defn sqSum [& args] (operation sqSumCalc args))
(defn meansq [& args] (operation meansqCalc args))
(defn rms [& args] (operation rmsCalc args))

(def strToFunc {"+" add "-" subtract "*" multiply "/" divide "negate" negate "meansq" meansq "rms" rms})
(def strToObject {"+" Add "-" Subtract "*" Multiply "/" Divide "negate" Negate "sumexp" Sumexp "lse" LSE "exp" Exp})

(defn primitive? [token]
    (or (number? token) (symbol? token))
)

(defn abstractParser [strToReprFunc constantCreator variableCreator]
    (fn [expr]
        (defn realParse [tokens]
            (cond
                (number? tokens) (constantCreator tokens)
                (symbol? tokens) (variableCreator (str tokens))
                :else (apply (get strToReprFunc (str (first tokens))) (map realParse (rest tokens)))
            )
        )
        (realParse (read-string expr))
    )
)

(def parseObject (abstractParser strToObject Constant Variable))
(def parseFunction (abstractParser strToFunc constant variable))

(def evaluate _evaluate)
(def toString _toString)
(def diff _diff)

; (def expr (Sumexp (Variable "x")))
; (println (evaluate expr {"x" 2}))

; (println (toString (_diff exprr "x")))

(def exprr (Sumexp (Variable "x") ))
(println (evaluate exprr {"x" 2}) )

; (def exprr
;   (Subtract
;     (Multiply
;       (Constant 2)
;       (Variable "x"))
;     (Constant 3)))
; (println (toString exprr))
; (println (evaluate exprr {"x" 2}))
; (println (_diff exprr "x"))
; (def exprr (divide (constant 1) (constant 2) ))
; (println (exprr {"x" 2}) )
; (println ((parseFunction "(- (* 2 x) 3)") {"x" 2}))