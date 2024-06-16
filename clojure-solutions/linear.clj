(defn sz [nDimensionVector]
    (if (number? nDimensionVector)
        []
        (if (every? number? nDimensionVector)
            (vector (count nDimensionVector))
            (conj (sz (first nDimensionVector)) (count nDimensionVector))
        )
    )
)

(defn isCorrectNdimensionVector [nDimensionVector]
    (or
        (every? number? nDimensionVector) (and
        (every? vector? nDimensionVector)

        (every? #(= (sz %) (sz (first nDimensionVector))) nDimensionVector))
    )
)

(defn doNdimensionOperation [operation valuesArray]
    (if (number? (first valuesArray))
        (reduce operation valuesArray)
        (let [summator (fn [x y] (doNdimensionOperation operation [x y] ))]
            (vec
                (reduce
                    #(mapv summator %1 %2)
                    valuesArray
                )
            )
        )
    )
)

(defn doOperation [operation valuesArray]
{:pre [(isCorrectNdimensionVector valuesArray)]
}
    (doNdimensionOperation operation valuesArray)
)

(defn matrixSize [matrix]
    [(count matrix) (count (first matrix))]
)

(defn isCorrectVectors [vectors]
    (and
        (every? vector? vectors)
        (every? #(every? number? %) vectors)
        (every? #(every? number? %) vectors)
        (every? #(= (count %) (count (first vectors))) vectors)
    )
)

(defn isMatrix [matrix]
    (and (vector? matrix)
        (isCorrectVectors matrix)
    )
)

(defn scalarMultiply [nDimensionVector, s]
    (if (number? nDimensionVector)
        (* nDimensionVector s)
        (vec (map #(scalarMultiply % s) nDimensionVector))
    )
)

(defn createTensor [tensorSize value]
    (if (empty? tensorSize)
        value
        (vec (repeat (first tensorSize) (createTensor (rest tensorSize) value)))
    )
)

(defn broadcast [prefixTensor mainTensor]
    (defn broadcastDecorator [prefixTensor mainTensorSize]
        (if (number? prefixTensor)
            (createTensor mainTensorSize prefixTensor)
            (vec (map #(broadcastDecorator % (rest mainTensorSize)) prefixTensor))
        )
    )
    (broadcastDecorator prefixTensor (reverse (sz mainTensor)))
)

(defn inverse [nDimensionVector]
    (if (number? nDimensionVector)
        (/ 1 nDimensionVector)
        (vec (map #(inverse %) nDimensionVector))
    )
)

(defn *s [& nDimensionVectors]
{:pre [
     (or (= (count nDimensionVectors) 1) (number? (last nDimensionVectors)))
    ]
}
    (def v (first nDimensionVectors))
    (if (= (count nDimensionVectors) 1)
        v
        (scalarMultiply v (second nDimensionVectors))
    )
)

(def v*s *s)
(defn v+ [& vectors] (doOperation + vectors))
(defn v* [& vectors] (doOperation * vectors))
(defn v- [& vectors]
{:pre [(isCorrectNdimensionVector vectors)]
}
      (if (= (count vectors) 1)
        (*s (first vectors) -1.0)
        (doNdimensionOperation - vectors)
      )
)
(defn vd [& vectors]
{:pre [(isCorrectNdimensionVector vectors)]
}
      (if (= (count vectors) 1)
          (inverse (first vectors))
          (doNdimensionOperation / vectors)
      )
)

(defn scalar [& vectors]
    (reduce + (apply v* vectors))
)

(def m+ v+)
(def m* v*)
(def m- v-)
(def md vd)
(def m*s *s)

(defn m*v [matrix v]
    (mapv #(scalar % v) matrix)
)

(defn transpose [matrix]
    (apply mapv vector matrix)
)

(defn crossProduct [vector1, vector2]
 (let [x1 (nth vector1 0) y1 (nth vector1 1) z1 (nth vector1 2)
        x2 (nth vector2 0) y2 (nth vector2 1) z2 (nth vector2 2)]
    [( - (* y1 z2) (* z1 y2))
     ( - (* z1 x2) (* x1 z2))
     ( - (* x1 y2) (* y1 x2))])
)

(defn vect [& vectors]
{:pre [(isCorrectVectors vectors)]
}
    vec (reduce crossProduct vectors)
)

(defn matrixMultiply [matrix1 matrix2]
    (vec (map #(m*v (transpose matrix2) %) matrix1))
)

(defn m*m [& matrices]
{:pre [
     (every? isMatrix matrices)
    ]
}
    vec (reduce #(matrixMultiply %1 %2) matrices)
)

(defn getMaxTensor [tensors]
    (reduce #(if (>= (count (sz %1)) (count (sz %2))) %1 %2) tensors)
)

(defn starts-with? [t1 t2]
    (and (>= (count t1) (count t2)) (= (take (count t2) t1) t2) )
)

(defn areBroadcastedTensors [tensors]
    (every? #(starts-with? (reverse (sz (getMaxTensor tensors))) %) (map #(reverse (sz %)) tensors))
)

(defn tensorOperation [operation]
    (fn op [& tensors]
        (apply operation (map #(broadcast % (getMaxTensor tensors)) tensors))
    )
)

(def tb+ (tensorOperation v+))
(def tb- (tensorOperation v-))
(def tb* (tensorOperation v*))
(def tbd (tensorOperation vd))

(def t+ tb+)
(def t- tb-)
(def t* tb*)
(def td tbd)

(println 1)