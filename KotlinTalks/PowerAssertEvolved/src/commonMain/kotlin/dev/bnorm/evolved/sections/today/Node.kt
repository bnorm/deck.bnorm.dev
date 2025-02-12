package dev.bnorm.evolved.sections.today

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.evolved.template.code.toCode

data class Node(
    val size: DpSize = DpSize(100.dp, 40.dp),
    val reveal: Int,
    val content: @Composable () -> Unit,
) {
    data class Link(
        val start: DpOffset,
        val end: DpOffset,
        val label: String? = null,
    )

    data class Child(
        val node: Node,
        val offset: DpOffset,
        val link: Link,
    )

    val children = mutableListOf<Child>()

    fun child(node: Node, offset: DpOffset, link: Link) {
        children.add(Child(node, offset, link))
    }
}

val EXPRESSION_TREE_ROOT = buildTree()

fun buildTree(): Node {
    val root = Node(
        reveal = 1,
        content = {
            Text("When")
            Text("str.length >= 1 && str[0] == 'x'".toCode(), fontSize = 4.sp, lineHeight = 4.sp)
        }
    )

    val condition1 = Node(
        reveal = root.reveal + 1,
        content = {
            Text("Chain")
        }
    ).apply {
        val node1 = Node(
            reveal = reveal + 1,
            content = {
                Text("Expr")
                Text("str".toCode(), fontSize = 8.sp, lineHeight = 8.sp)
            }
        )
        val node2 = Node(
            reveal = reveal + 2,
            content = {
                Text("Expr")
                Text("str.length".toCode(), fontSize = 8.sp, lineHeight = 8.sp)
            }
        )
        val node3 = Node(
            reveal = reveal + 3,
            content = {
                Text("Const")
                Text("1".toCode(), fontSize = 8.sp, lineHeight = 8.sp)
            }
        )
        val node4 = Node(
            reveal = reveal + 4,
            content = {
                Text("Expr")
                Text("str.length >= 1".toCode(), fontSize = 8.sp, lineHeight = 8.sp)
            }
        )

        child(
            node = node1,
            offset = DpOffset(0.dp + 25.dp, size.height + 15.dp),
            link = Node.Link(
                start = DpOffset(size.width / 2, size.height),
                end = DpOffset(node1.size.width / 2, 0.dp)
            )
        )
        child(
            node = node2,
            offset = DpOffset(105.dp + 25.dp, size.height + 15.dp),
            link = Node.Link(
                start = DpOffset(size.width / 2, size.height),
                end = DpOffset(node2.size.width / 2, 0.dp)
            )
        )
        child(
            node = node3,
            offset = DpOffset(210.dp + 25.dp, size.height + 15.dp),
            link = Node.Link(
                start = DpOffset(size.width / 2, size.height),
                end = DpOffset(node3.size.width / 2, 0.dp)
            )
        )
        child(
            node = node4,
            offset = DpOffset(315.dp + 25.dp, size.height + 15.dp),
            link = Node.Link(
                start = DpOffset(size.width / 2, size.height),
                end = DpOffset(node4.size.width / 2, 0.dp)
            )
        )
    }

    val branch1 = Node(
        reveal = condition1.children.last().node.reveal + 1,
        content = {
            Text("Chain")
        }
    ).apply {
        val node1 = Node(
            reveal = reveal + 1,
            content = {
                Text("Expr")
                Text("str".toCode(), fontSize = 8.sp, lineHeight = 8.sp)
            }
        )
        val node2 = Node(
            reveal = reveal + 2,
            content = {
                Text("Const")
                Text("0".toCode(), fontSize = 8.sp, lineHeight = 8.sp)
            }
        )
        val node3 = Node(
            reveal = reveal + 3,
            content = {
                Text("Expr")
                Text("str[0]".toCode(), fontSize = 8.sp, lineHeight = 8.sp)
            }
        )
        val node4 = Node(
            reveal = reveal + 4,
            content = {
                Text("Const")
                Text("'x'".toCode(), fontSize = 8.sp, lineHeight = 8.sp)
            }
        )
        val node5 = Node(
            reveal = reveal + 5,
            content = {
                Text("Expr")
                Text("str[0] == 'x'".toCode(), fontSize = 8.sp, lineHeight = 8.sp)
            }
        )

        child(
            node = node1,
            offset = DpOffset(0.dp + 25.dp, size.height + 15.dp),
            link = Node.Link(
                start = DpOffset(size.width / 2, size.height),
                end = DpOffset(node1.size.width / 2, 0.dp)
            )
        )
        child(
            node = node2,
            offset = DpOffset(105.dp + 25.dp, size.height + 15.dp),
            link = Node.Link(
                start = DpOffset(size.width / 2, size.height),
                end = DpOffset(node1.size.width / 2, 0.dp)
            )
        )
        child(
            node = node3,
            offset = DpOffset(210.dp + 25.dp, size.height + 15.dp),
            link = Node.Link(
                start = DpOffset(size.width / 2, size.height),
                end = DpOffset(node2.size.width / 2, 0.dp)
            )
        )
        child(
            node = node4,
            offset = DpOffset(315.dp + 25.dp, size.height + 15.dp),
            link = Node.Link(
                start = DpOffset(size.width / 2, size.height),
                end = DpOffset(node3.size.width / 2, 0.dp)
            )
        )
        child(
            node = node5,
            offset = DpOffset(420.dp + 25.dp, size.height + 15.dp),
            link = Node.Link(
                start = DpOffset(size.width / 2, size.height),
                end = DpOffset(node4.size.width / 2, 0.dp)
            )
        )
    }

    val condition2 = Node(
        reveal = branch1.children.last().node.reveal + 1,
        content = {
            Text("Const")
            Text("true".toCode(), fontSize = 8.sp, lineHeight = 8.sp)
        }
    )

    val branch2 = Node(
        reveal = condition2.reveal + 1,
        content = {
            Text("Const")
            Text("false".toCode(), fontSize = 8.sp, lineHeight = 8.sp)
        }
    )

    return root.apply {
        child(
            node = condition1,
            offset = DpOffset(size.width + 100.dp, (-137.5).dp),
            link = Node.Link(
                start = DpOffset(size.width, size.height / 2),
                end = DpOffset(0.dp, condition1.size.height / 2),
                label = "Condition"
            )
        )
        child(
            node = branch1,
            offset = DpOffset(size.width + 100.dp, (-27.5).dp),
            link = Node.Link(
                start = DpOffset(size.width, size.height / 2),
                end = DpOffset(0.dp, branch1.size.height / 2),
                label = "Branch"
            )
        )
        child(
            node = condition2,
            offset = DpOffset(size.width + 100.dp, 82.5.dp),
            link = Node.Link(
                start = DpOffset(size.width, size.height / 2),
                end = DpOffset(0.dp, condition2.size.height / 2),
                label = "Condition"
            )
        )
        child(
            node = branch2,
            offset = DpOffset(size.width + 100.dp, 137.5.dp),
            link = Node.Link(
                start = DpOffset(size.width, size.height / 2),
                end = DpOffset(0.dp, branch2.size.height / 2),
                label = "Branch"
            )
        )
    }
}
