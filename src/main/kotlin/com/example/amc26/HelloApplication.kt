package com.example.amc26

import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundImage
import javafx.scene.layout.BackgroundPosition
import javafx.scene.layout.BackgroundRepeat
import javafx.scene.layout.BackgroundSize
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.stage.Stage
import java.nio.file.Path

class HelloApplication : Application() {
    val pane: Pane = Pane()
    val scene: Scene = Scene(pane, WIDTH, HEIGHT)
    val qstPane: Pane = Pane()
    val controller: GameController = GameController(this)
    val streakManager: StreakManager = StreakManager(Path.of("src/main/resources/infos.json"))
    var currentPlan: Plan = Plan.ACCUEIL

    override fun start(stage: Stage) {
        streakManager.checkStreakOnLaunch()
        accueil()

        val timer = object : AnimationTimer() {
            override fun handle(now: Long) {
            }
        }
        timer.start()

        stage.title = "Hello!"
        stage.scene = scene
        stage.show()
    }

    fun loadNewPlan(plan: Plan) {
        pane.children.clear()
        pane.background = null
        currentPlan = plan
        when (plan) {
            Plan.ACCUEIL -> accueil()
            Plan.GAMEPLAY -> questionMenu()
            Plan.RESULTS -> results()
            Plan.OPTIONS -> options()
        }
    }

    fun accueil() {
        val hBox = HBox().apply {
            setPrefSize(WIDTH, HEIGHT)
            alignment = Pos.CENTER
        }

        val streakBox = HBox(10.0).apply {
            setPrefSize(Int.MIN_VALUE.toDouble(), Int.MIN_VALUE.toDouble())
            alignment = Pos.CENTER
        }

        val streak = Label(streakManager.streakCount.toString()).apply {
            textFill = Color.CHOCOLATE
        }

        val streakImage = Image(javaClass.getResource("/images/streakIcon.png")!!.toExternalForm())
        val imageView = ImageView(streakImage).apply {
            fitHeight = 64.0
            fitWidth = 64.0
        }

        val menuBackground = Image(javaClass.getResource("/images/menuBackground.jpg")!!.toExternalForm())
        val backgroundImage = BackgroundImage(
            menuBackground,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            BackgroundSize(100.0, 100.0, true, true, false, true)
        )

        val vBox = VBox(5.0).apply {
            background = Background(backgroundImage)
            setPrefSize(WIDTH, HEIGHT)
            alignment = Pos.CENTER
        }

        val btnGameplay = Button("Lancer le quiz !").apply {
            setOnAction { loadNewPlan(Plan.GAMEPLAY) }
        }

        val btnOptions = Button("Options").apply {
            setOnAction { loadNewPlan(Plan.OPTIONS) }
        }

        streakBox.children.addAll(streak, imageView)
        vBox.children.addAll(streakBox, btnGameplay, btnOptions)
        hBox.children.addAll(vBox)
        pane.children.add(hBox)
    }

    fun questionMenu() {
        controller.nextQuestion()
        if (currentPlan == Plan.GAMEPLAY) {
            pane.children.clear()
            qstPane.children.clear()

            val theme = controller.getTheme()
            val background = getRightBackground(theme)

            if (background != null) {
                val backgroundImage = BackgroundImage(
                    background,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    BackgroundSize(100.0, 100.0, true, true, false, true)
                )
                pane.background = Background(backgroundImage)
            }

            val button = Button("<-").apply {
                setOnAction { loadNewPlan(Plan.ACCUEIL) }
            }
            pane.children.add(button)

            val hBoxTheme = HBox().apply {
                layoutY = 50.0
                prefWidth = WIDTH
                alignment = Pos.CENTER
            }
            val lTheme = Label(theme).apply {
                font = Font.font("Arial", FontWeight.BOLD, 24.0)
            }
            hBoxTheme.children.add(lTheme)
            qstPane.children.add(hBoxTheme)

            val hBoxStatement = HBox().apply {
                layoutY = 100.0
                prefWidth = WIDTH
                alignment = Pos.CENTER
            }
            val lStatement = Label(controller.getStatement()).apply {
                font = Font.font("Arial", FontWeight.EXTRA_BOLD, 12.0)
                maxWidth = WIDTH - 40
                isWrapText = true
                textFill = Color.CHOCOLATE
            }
            hBoxStatement.children.add(lStatement)
            qstPane.children.add(hBoxStatement)

            val hBoxBtns = HBox(50.0).apply {
                layoutY = 250.0
                prefWidth = WIDTH
                alignment = Pos.CENTER
            }
            val btnVrai = Button("Vrai").apply {
                setPrefSize(100.0, 75.0)
            }
            val btnMythe = Button("Mythe").apply {
                setPrefSize(100.0, 75.0)
            }
            btnVrai.setOnAction {
                clickButton(hBoxBtns, btnVrai, btnMythe, lStatement, true)
            }
            btnMythe.setOnAction {
                clickButton(hBoxBtns, btnVrai, btnMythe, lStatement, false)
            }
            hBoxBtns.children.addAll(btnVrai, btnMythe)
            qstPane.children.add(hBoxBtns)

            pane.children.add(qstPane)
        }
    }

    private fun clickButton(
        hBoxBtns: HBox,
        btnVrai: Button,
        btnMythe: Button,
        lStatement: Label,
        buttonValue: Boolean
    ) {
        controller.verifyAnswer(buttonValue)
        hBoxBtns.children.removeAll(btnVrai, btnMythe)

        val lTruth = Label(controller.getTruth()).apply {
            font = Font.font(20.0)
            textFill = if (controller.getTruthValue()) Color.GREEN else Color.RED
        }

        val lFact = Label(controller.getFact()).apply {
            font = Font.font(12.0)
            maxWidth = WIDTH - 40
            isWrapText = true
        }

        val btnContinue = Button("Continue").apply {
            setOnAction { questionMenu() }
        }

        val vBox = VBox(10.0).apply {
            children.addAll(lTruth, lStatement, btnContinue)
        }
        hBoxBtns.children.add(vBox)
    }

    fun results() {
        streakManager.recordAnsweredToday()
        val hBox = HBox().apply {
            layoutY = 100.0
            setPrefSize(WIDTH, HEIGHT)
            alignment = Pos.CENTER
        }

        val label = Label("${controller.nbCorrectAnswers}/${GameController.NB_QUESTIONS}")
        hBox.children.addAll(label)
        pane.children.add(hBox)
    }

    fun options() {
        val hBox = HBox().apply {
            setPrefSize(WIDTH, HEIGHT)
            alignment = Pos.CENTER
        }

        val vBox = VBox(5.0).apply {
            setPrefSize(WIDTH, HEIGHT)
            alignment = Pos.CENTER
        }

        val btnImage = Button("Put an image as the background").apply {
            setOnAction {
                // Image => be chosen by the user
            }
        }

        vBox.children.addAll(btnImage)
        hBox.children.addAll(vBox)
        pane.children.add(hBox)
    }

    fun getRightBackground(theme: String): Image? {
        println(theme)

        val resourcePath = when (theme) {
            "Hiver" -> "/images/hiverBackground.jpg"
            "Mobilité" -> "/images/mobiliteBackground.jpg"
            "Quartier & Écologie" -> "/images/quartierBackground.jpg"
            "Culture & Quotidien" -> "/images/cultureBackground.jpg"
            else -> null
        } ?: return null

        return javaClass.getResource(resourcePath)?.toExternalForm()?.let { Image(it) }
    }

    companion object {
        const val WIDTH: Double = 300.0
        const val HEIGHT: Double = 533.0
    }
}
